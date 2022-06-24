package com.sap.mobile.services.client.validation.broker.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Sets;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.cloudfoundry.client.facade.CloudControllerClient;
import com.sap.cloudfoundry.client.facade.domain.CloudServiceInstance;
import com.sap.cloudfoundry.client.facade.domain.CloudServiceKey;
import com.sap.cloudfoundry.client.facade.domain.ImmutableCloudServiceInstance;
import com.sap.cloudfoundry.client.facade.domain.ServiceOperation;
import com.sap.mobile.services.client.validation.broker.configuration.bean.MobileServicesConfig;
import com.sap.mobile.services.client.validation.broker.exception.InstanceCreationFailedException;
import com.sap.mobile.services.client.validation.broker.exception.InstanceCreationTimeoutException;
import com.sap.mobile.services.client.validation.broker.exception.MaxConcurrentInstancesReachedException;
import com.sap.mobile.services.client.validation.broker.exception.NoSuchServiceInstanceException;
import com.sap.mobile.services.client.validation.broker.model.ServiceInstanceParams;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrokerService {

	public static final Set<String> ALLOWED_FEATURES = Collections.unmodifiableSet(Sets.newHashSet("storage", "push"));

	private final MobileServicesConfig config;
	private final CloudControllerClient cfClient;
	private final MobileServicesCockpitClient cockpitClient;
	private final ObjectMapper objectMapper;

	public Map<String, ?> createMobileApplication(final Set<String> requestFeatures) throws MaxConcurrentInstancesReachedException, InstanceCreationFailedException, InstanceCreationTimeoutException {
		if (config.getMaxInstances() != 0) {
			final int runningInstances = getRunningInstances().size();
			if (runningInstances >= config.getMaxInstances()) {
				throw new MaxConcurrentInstancesReachedException(config.getMaxInstances());
			}
		}

		final Collection<String> features = CollectionUtils.intersection(requestFeatures, ALLOWED_FEATURES);
		final String name = config.getNamePrefix() + RandomStringUtils.randomAlphabetic(32);

		final ServiceInstanceParams params = ServiceInstanceParams.builder()
				.name(name)
				.displayName(name)
				.security(ServiceInstanceParams.SecurityConfig.builder().name("oauth").build())
				.features(features.stream().map(f -> {
					return ServiceInstanceParams.Feature.builder().name(f).build();
				}).collect(Collectors.toList()))
				.build();

		final ImmutableCloudServiceInstance.Builder builder = ImmutableCloudServiceInstance.builder()
				.name(name)
				.label(config.getServiceName())
				.plan(config.getPlanName())
				.credentials(objectMapper.convertValue(params, new TypeReference<>() {
				}));

		if (StringUtils.isNotBlank(config.getBrokerName())) {
			builder.broker(config.getBrokerName());
		}

		cfClient.createServiceInstance(builder.build());
		final CloudServiceInstance createdInstance = waitForServiceInstanceCreation(name);
		final CloudServiceKey serviceKey = cfClient.createServiceKey(name, "integration-tests", Collections.emptyMap());

		cockpitClient.restoreApp(createdInstance);

		return serviceKey.getCredentials();
	}

	@Scheduled(fixedDelayString = "PT5M")
	public void runCleanUp() {
		final LocalDateTime now = LocalDateTime.now(ZoneId.of("GMT"));
		final LocalDateTime cleanupTime = now.minus(config.getMobileApplicationLifetime());

		final List<CloudServiceInstance> instancesToDelete = getRunningInstances().stream()
				.filter(instance -> instance.getMetadata().getCreatedAt().isBefore(cleanupTime))
				.collect(Collectors.toList());

		instancesToDelete.forEach(this::deleteServiceInstanceWithDependencies);
	}

	private void deleteServiceInstanceWithDependencies(final CloudServiceInstance instance) {
		cfClient.getServiceKeys(instance).forEach(cfClient::deleteServiceKey);
		cfClient.getServiceBindings(instance.getGuid()).forEach(binding -> {
			cfClient.unbindServiceInstance(binding.getApplicationGuid(), instance.getGuid());
		});
		cockpitClient.deleteApp(instance);
	}

	private List<CloudServiceInstance> getRunningInstances() {
		final List<CloudServiceInstance> allServiceInstances = cfClient.getServiceInstances();
		return allServiceInstances.stream()
				.filter(instance -> instance.getLabel().equals(config.getServiceName()))
				.filter(instance -> instance.getPlan().equals(config.getPlanName()))
				.filter(instance -> StringUtils.isBlank(config.getBrokerName()) || instance.getBroker().equals(config.getBrokerName()))
				.filter(instance -> instance.getName().startsWith(config.getNamePrefix()))
				.collect(Collectors.toList());
	}

	private CloudServiceInstance waitForServiceInstanceCreation(final String name) throws InstanceCreationTimeoutException, InstanceCreationFailedException {
		try {
			final CloudServiceInstance instance = Awaitility.await().atMost(Duration.ofMinutes(3))
					.with()
					.pollDelay(Duration.ofSeconds(5))
					.until(() -> {
						return cfClient.getServiceInstance(name, true);
					}, (i -> i.getLastOperation().getState() != ServiceOperation.State.IN_PROGRESS));

			if (instance.getLastOperation().getState() != ServiceOperation.State.SUCCEEDED) {
				throw new InstanceCreationFailedException();
			}

			return instance;
		} catch (ConditionTimeoutException e) {
			throw new InstanceCreationTimeoutException();
		}
	}


	public void deleteMobileApplication(final String appId) throws NoSuchServiceInstanceException {
		final CloudServiceInstance instance = Optional.ofNullable(cfClient.getServiceInstance(appId, false))
				.orElseThrow(() -> new NoSuchServiceInstanceException(appId));
		deleteServiceInstanceWithDependencies(instance);
	}
}

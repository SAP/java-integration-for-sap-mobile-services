package com.sap.mobile.services.client.validation.broker.service;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
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
import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.v2.routes.ListRoutesRequest;
import org.cloudfoundry.client.v2.routes.RouteResource;
import org.cloudfoundry.client.v2.serviceinstances.CreateServiceInstanceRequest;
import org.cloudfoundry.client.v2.serviceinstances.CreateServiceInstanceResponse;
import org.cloudfoundry.client.v2.servicekeys.CreateServiceKeyRequest;
import org.cloudfoundry.client.v2.servicekeys.CreateServiceKeyResponse;
import org.cloudfoundry.client.v2.servicekeys.ListServiceKeysRequest;
import org.cloudfoundry.client.v2.servicekeys.ListServiceKeysResponse;
import org.cloudfoundry.client.v2.servicekeys.ServiceKeyEntity;
import org.cloudfoundry.client.v2.servicekeys.ServiceKeyResource;
import org.cloudfoundry.client.v3.Metadata;
import org.cloudfoundry.client.v3.domains.GetDomainRequest;
import org.cloudfoundry.client.v3.serviceinstances.ListServiceInstancesRequest;
import org.cloudfoundry.client.v3.serviceinstances.ListServiceInstancesResponse;
import org.cloudfoundry.client.v3.serviceinstances.ServiceInstanceResource;
import org.cloudfoundry.client.v3.serviceinstances.UpdateServiceInstanceRequest;
import org.cloudfoundry.client.v3.serviceplans.ServicePlanResource;
import org.cloudfoundry.client.v3.spaces.SpaceResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.mobile.services.client.validation.broker.configuration.bean.MobileServicesConfig;
import com.sap.mobile.services.client.validation.broker.exception.InstanceCreationFailedException;
import com.sap.mobile.services.client.validation.broker.exception.InstanceCreationTimeoutException;
import com.sap.mobile.services.client.validation.broker.exception.MaxConcurrentInstancesReachedException;
import com.sap.mobile.services.client.validation.broker.exception.NoSuchServiceInstanceException;
import com.sap.mobile.services.client.validation.broker.model.AppConfig;
import com.sap.mobile.services.client.validation.broker.model.ServiceInstanceParams;
import com.sap.mobile.services.client.validation.broker.model.ServiceKeyRequest;
import com.sap.mobile.services.client.validation.broker.service.api.BrokerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrokerServiceImpl implements BrokerService {

	public static final Set<String> ALLOWED_FEATURES = Collections.unmodifiableSet(Sets.newHashSet("storage", "push"));

	private final MobileServicesConfig config;
	private final CloudFoundryClient cfClient;
	private final SpaceResource spaceResource;
	private final ServicePlanResource servicePlanResource;
	private final MobileServicesCockpitClient cockpitClient;
	private final ObjectMapper objectMapper;

	@Override
	public Map<String, ?> createMobileApplication(final Set<String> requestFeatures) throws MaxConcurrentInstancesReachedException, InstanceCreationFailedException, InstanceCreationTimeoutException {
		return createMobileApplication(requestFeatures, Collections.singletonMap("managed-by", "mobile-services-test-broker"));
	}

	Map<String, ?> createMobileApplication(final Set<String> requestFeatures, final Map<String, String> labels) throws MaxConcurrentInstancesReachedException, InstanceCreationFailedException, InstanceCreationTimeoutException {
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

		final CreateServiceInstanceResponse response = cfClient.serviceInstances()
				.create(CreateServiceInstanceRequest.builder()
						.name(name)
						.servicePlanId(servicePlanResource.getId())
						.spaceId(spaceResource.getId())
						.acceptsIncomplete(true)
						.parameters(objectMapper.convertValue(params, new TypeReference<>() {
						}))
						.build())
				.block();
		ServiceInstanceResource instance = waitForServiceInstanceCreation(name);

		cfClient.serviceInstancesV3().update(UpdateServiceInstanceRequest.builder()
				.serviceInstanceId(response.getMetadata().getId())
				.metadata(Metadata.builder()
						.labels(labels)
						.build())
				.build()
		).block();
		instance = waitForServiceInstanceCreation(name);

		final CreateServiceKeyResponse serviceKeyResponse = cfClient.serviceKeys().create(CreateServiceKeyRequest.builder()
						.name("integration-tests")
						.serviceInstanceId(response.getMetadata().getId())
						.build())
				.block();

		cockpitClient.restoreApp(instance);

		return serviceKeyResponse.getEntity().getCredentials();
	}


	@Override
	public Map<String, ?> getMobileApplicationKey(final String appId) throws NoSuchServiceInstanceException {
		final ServiceInstanceResource instance = getMobileApplication(appId);

		return cfClient.serviceKeys().list(ListServiceKeysRequest.builder()
						.name("integration-tests")
						.serviceInstanceId(instance.getId())
						.build())
				.map(ListServiceKeysResponse::getResources)
				.flatMap(bindings -> bindings.size() > 0 ? Mono.just(bindings.get(0)) : Mono.empty())
				.blockOptional()
				.map(ServiceKeyResource::getEntity)
				.map(ServiceKeyEntity::getCredentials)
				.orElseGet(() -> {
					return cfClient.serviceKeys().create(CreateServiceKeyRequest.builder()
									.name("integration-tests")
									.serviceInstanceId(instance.getId())
									.build())
							.block().getEntity().getCredentials();
				});
	}

	@Override
	public AppConfig createMobileServicesSettingsConfig(final String appId, final ServiceKeyRequest serviceKeyRequest) throws NoSuchServiceInstanceException {
		final ServiceInstanceResource instance = getMobileApplication(appId);
		final Map<?, ?> serviceKey = cockpitClient.createServiceKey(instance, serviceKeyRequest);
		final URI serverUrl = getMobileAppUrl(instance).orElseGet(() -> {
			log.warn("App instance {} has no route service bound", appId);
			return null;
		});

		return AppConfig.builder()
				.applicationId(appId)
				.platform("CF")
				.server(serverUrl)
				.services(Collections.singletonList(
						AppConfig.AppService.builder()
								.name(serviceKeyRequest.getServiceName())
								.serviceKeys(Collections.singletonList(serviceKey))
								.build()))
				.build();
	}

	@Override
	public void deleteMobileApplication(final String appId) throws NoSuchServiceInstanceException {
		final ServiceInstanceResource instance = getMobileApplication(appId);
		deleteServiceInstanceWithDependencies(instance);
	}

	@Scheduled(fixedDelayString = "PT5M")
	public void runCleanUp() {
		final Instant now = Instant.now();
		final Instant cleanupTime = now.minus(config.getMobileApplicationLifetime());

		final List<ServiceInstanceResource> instancesToDelete = getRunningInstances().stream()
				.filter(instance -> {
					final Instant createdAt = Instant.parse(instance.getCreatedAt());
					return createdAt.isBefore(cleanupTime);
				})
				.collect(Collectors.toList());

		instancesToDelete.forEach(this::deleteServiceInstanceWithDependencies);
	}

	private void deleteServiceInstanceWithDependencies(final ServiceInstanceResource instance) {
		cockpitClient.deleteApp(instance);
	}

	private List<ServiceInstanceResource> getRunningInstances() {
		return PaginationUtils.paginate(page -> {
					return ListServiceInstancesRequest.builder()
							.spaceId(spaceResource.getId())
							.labelSelector("managed-by=mobile-services-test-broker")
							.page(page)
							.build();
				}, cfClient.serviceInstancesV3()::list)
				.filter(instance -> instance.getName().startsWith(config.getNamePrefix()))
				.filter(instance -> instance.getRelationships().getServicePlan().getData().getId().equals(servicePlanResource.getId()))
				.collectList().block();
	}

	private ServiceInstanceResource waitForServiceInstanceCreation(final String name) throws InstanceCreationTimeoutException, InstanceCreationFailedException {
		try {
			final ServiceInstanceResource instance = Awaitility.await().atMost(Duration.ofMinutes(3))
					.with()
					.pollDelay(Duration.ofSeconds(5))
					.until(() -> {
						return cfClient.serviceInstancesV3().list(ListServiceInstancesRequest.builder()
										.serviceInstanceName(name)
										.spaceId(spaceResource.getId())
										.build())
								.map(ListServiceInstancesResponse::getResources)
								.map(l -> l.get(0))
								.block();
					}, (i -> !i.getLastOperation().getState().equals("in progress")));

			if (!instance.getLastOperation().getState().equals("succeeded")) {
				throw new InstanceCreationFailedException();
			}

			return instance;
		} catch (ConditionTimeoutException e) {
			throw new InstanceCreationTimeoutException();
		}
	}

	private ServiceInstanceResource getMobileApplication(final String appId) throws NoSuchServiceInstanceException {
		return cfClient.serviceInstancesV3().list(ListServiceInstancesRequest.builder()
						.spaceId(spaceResource.getId())
						.serviceInstanceName(appId)
						.build()
				).map(ListServiceInstancesResponse::getResources)
				.flatMap(instances -> instances.size() > 0 ? Mono.just(instances.get(0)) : Mono.empty())
				.blockOptional()
				.orElseThrow(() -> new NoSuchServiceInstanceException(appId));
	}

	private Optional<URI> getMobileAppUrl(final ServiceInstanceResource appInstance) throws NoSuchServiceInstanceException {
		return Optional.ofNullable(PaginationUtils.paginateV2(page -> {
					return ListRoutesRequest.builder().organizationId(spaceResource.getRelationships().getOrganization().getData().getId())
							.page(page).build();
				}, cfClient.routes()::list)
				.map(RouteResource::getEntity)
				.filter(r -> StringUtils.equals(r.getServiceInstanceId(), appInstance.getId()))
				.collectList()
				.map(l -> l.stream().findFirst())
				.flatMap(optionalRoute -> {
					return optionalRoute.map(r -> {
						return cfClient.domainsV3().get(GetDomainRequest.builder()
								.domainId(r.getDomainId())
								.build()).map(domain -> {
							final String hostname = r.getHost() + "." + domain.getName();
							return UriComponentsBuilder.newInstance().scheme("https").host(hostname).path(r.getPath()).build().toUri();
						});
					}).orElseGet(Mono::empty);
				}).block());
	}
}

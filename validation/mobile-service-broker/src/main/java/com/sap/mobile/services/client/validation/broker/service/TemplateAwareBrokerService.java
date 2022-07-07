package com.sap.mobile.services.client.validation.broker.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.v3.Metadata;
import org.cloudfoundry.client.v3.serviceinstances.ListServiceInstancesRequest;
import org.cloudfoundry.client.v3.serviceinstances.ServiceInstanceResource;
import org.cloudfoundry.client.v3.serviceinstances.UpdateServiceInstanceRequest;
import org.cloudfoundry.client.v3.serviceplans.ServicePlanResource;
import org.cloudfoundry.client.v3.spaces.SpaceResource;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sap.mobile.services.client.validation.broker.configuration.bean.BrokerTemplateConfig;
import com.sap.mobile.services.client.validation.broker.configuration.bean.MobileServicesConfig;
import com.sap.mobile.services.client.validation.broker.exception.InstanceCreationFailedException;
import com.sap.mobile.services.client.validation.broker.exception.InstanceCreationTimeoutException;
import com.sap.mobile.services.client.validation.broker.exception.MaxConcurrentInstancesReachedException;
import com.sap.mobile.services.client.validation.broker.exception.NoSuchServiceInstanceException;
import com.sap.mobile.services.client.validation.broker.service.api.BrokerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class TemplateAwareBrokerService implements BrokerService {

	private final BrokerServiceImpl delegate;
	private final MobileServicesConfig config;
	private final CloudFoundryClient cfClient;
	private final TaskExecutor taskExecutor;
	private final SpaceResource spaceResource;
	private final ServicePlanResource servicePlanResource;

	@Override
	public Map<String, ?> createMobileApplication(final Set<String> requestFeatures) throws MaxConcurrentInstancesReachedException, InstanceCreationFailedException, InstanceCreationTimeoutException {
		final Set<String> featuresWithoutStorage = new HashSet<>(requestFeatures);
		featuresWithoutStorage.remove("storage");
		final Optional<BrokerTemplateConfig> template = config.getTemplates().stream()
				.filter(t -> t.getFeatures().equals(featuresWithoutStorage))
				.findFirst();

		if (template.isPresent()) {
			return createMobileApplication(template.get());
		} else {
			return delegate.createMobileApplication(requestFeatures);
		}
	}

	@Override
	public Map<String, ?> getMobileApplicationKey(final String appId) throws NoSuchServiceInstanceException {
		return delegate.getMobileApplicationKey(appId);
	}

	private Map<String, ?> createMobileApplication(final BrokerTemplateConfig templateConfig) throws MaxConcurrentInstancesReachedException, InstanceCreationFailedException, InstanceCreationTimeoutException {
		final List<ServiceInstanceResource> instances = getRunningTemplates(templateConfig);
		if (instances.isEmpty()) {
			refreshTemplateAsync(templateConfig);
			return delegate.createMobileApplication(templateConfig.getFeatures());
		}

		final ServiceInstanceResource instance = instances.get(0);
		cfClient.serviceInstancesV3().update(UpdateServiceInstanceRequest.builder()
				.serviceInstanceId(instance.getId())
				.metadata(Metadata.builder()
						.label("managed-by", "mobile-services-test-broker")
						.build())
				.build()).block();

		refreshTemplateAsync(templateConfig);
		try {
			return delegate.getMobileApplicationKey(instance.getName());
		} catch (NoSuchServiceInstanceException e) {
			throw new InstanceCreationFailedException();
		}
	}

	private void refreshTemplateAsync(final BrokerTemplateConfig templateConfig) {
		taskExecutor.execute(() -> {
			refreshTemplateSync(templateConfig);
		});
	}

	private void refreshTemplateSync(final BrokerTemplateConfig templateConfig) {
		try {
			synchronized (templateConfig) {
				final Map<String, String> labels = Collections.singletonMap("managed-by", "mobile-services-test-broker-template-" + templateConfig.getId());
				int templates = getRunningTemplates(templateConfig).size();
				while (templates < templateConfig.getKeepNumber()) {
					delegate.createMobileApplication(templateConfig.getFeatures(), labels);
					templates = getRunningTemplates(templateConfig).size();
				}
			}
		} catch (Exception e) {
			log.error("Failed to refresh templates", e);
		}
	}

	@Override
	public void deleteMobileApplication(final String appId) throws NoSuchServiceInstanceException {
		delegate.deleteMobileApplication(appId);
	}

	@Scheduled(fixedDelayString = "PT5M")
	public void checkTemplateState() {
		config.getTemplates().forEach(this::refreshTemplateSync);
	}

	private List<ServiceInstanceResource> getRunningTemplates(final BrokerTemplateConfig templateConfig) {
		return PaginationUtils.paginate(page -> {
					return ListServiceInstancesRequest.builder()
							.spaceId(spaceResource.getId())
							.labelSelector("managed-by=mobile-services-test-broker-template-" + templateConfig.getId())
							.page(page)
							.build();
				}, cfClient.serviceInstancesV3()::list)
				.filter(instance -> instance.getName().startsWith(config.getNamePrefix()))
				.filter(instance -> instance.getLastOperation().getState().equals("succeeded"))
				.filter(instance -> instance.getRelationships().getServicePlan().getData().getId().equals(servicePlanResource.getId()))
				.collectList().block();
	}
}

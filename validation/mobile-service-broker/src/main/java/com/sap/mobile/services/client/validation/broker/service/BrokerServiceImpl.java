package com.sap.mobile.services.client.validation.broker.service;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
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
import org.cloudfoundry.client.v3.Relationship;
import org.cloudfoundry.client.v3.ToOneRelationship;
import org.cloudfoundry.client.v2.routes.ListRoutesRequest;
import org.cloudfoundry.client.v2.routes.RouteResource;
import org.cloudfoundry.client.v2.servicekeys.CreateServiceKeyRequest;
import org.cloudfoundry.client.v2.servicekeys.CreateServiceKeyResponse;
import org.cloudfoundry.client.v2.servicekeys.ListServiceKeysRequest;
import org.cloudfoundry.client.v2.servicekeys.ListServiceKeysResponse;
import org.cloudfoundry.client.v2.servicekeys.ServiceKeyEntity;
import org.cloudfoundry.client.v2.servicekeys.ServiceKeyResource;
import org.cloudfoundry.client.v3.Metadata;
import org.cloudfoundry.client.v3.domains.GetDomainRequest;
import org.cloudfoundry.client.v3.serviceinstances.CreateServiceInstanceRequest;
import org.cloudfoundry.client.v3.serviceinstances.CreateServiceInstanceResponse;
import org.cloudfoundry.client.v3.serviceinstances.ListServiceInstancesRequest;
import org.cloudfoundry.client.v3.serviceinstances.ListServiceInstancesResponse;
import org.cloudfoundry.client.v3.serviceinstances.ServiceInstanceResource;
import org.cloudfoundry.client.v3.serviceinstances.ServiceInstanceRelationships;
import org.cloudfoundry.client.v3.serviceinstances.ServiceInstanceType;
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
	private static final Duration SERVICE_KEY_RETRY_DELAY = Duration.ofSeconds(5);
	private static final Duration SERVICE_KEY_RETRY_TIMEOUT = Duration.ofMinutes(2);

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

		final CreateServiceInstanceResponse response = cfClient.serviceInstancesV3()
				.create(CreateServiceInstanceRequest.builder()
						.name(name)
						.type(ServiceInstanceType.MANAGED)
						.relationships(ServiceInstanceRelationships.builder()
								.servicePlan(ToOneRelationship.builder()
										.data(Relationship.builder().id(servicePlanResource.getId()).build())
										.build())
								.space(ToOneRelationship.builder()
										.data(Relationship.builder().id(spaceResource.getId()).build())
										.build())
								.build())
						.parameters(objectMapper.convertValue(params, new TypeReference<>() {
						}))
						.build())
				.block();
		ServiceInstanceResource instance = waitForServiceInstanceCreation(name);
		final String serviceInstanceId = response.getServiceInstance()
				.map(ServiceInstanceResource::getId)
				.orElse(instance.getId());

		cfClient.serviceInstancesV3().update(UpdateServiceInstanceRequest.builder()
				.serviceInstanceId(serviceInstanceId)
				.metadata(Metadata.builder()
						.labels(labels)
						.build())
				.build()
		).block();
		instance = waitForServiceInstanceCreation(name);

		final Map<String, ?> serviceKeyCredentials = createOrGetIntegrationServiceKeyCredentials(serviceInstanceId);

		cockpitClient.restoreApp(instance);

		return serviceKeyCredentials;
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
					}, (i -> isTerminalState(i.getLastOperation().getState())));

			if (!isSucceededState(instance.getLastOperation().getState())) {
				log.error("Service instance creation ended in non-success state: state='{}', description='{}', instance='{}'",
						instance.getLastOperation().getState(),
						instance.getLastOperation().getDescription(),
						name);
				throw new InstanceCreationFailedException();
			}

			return instance;
		} catch (ConditionTimeoutException e) {
			throw new InstanceCreationTimeoutException();
		}
	}

	private static boolean isTerminalState(final String state) {
		final String normalized = Optional.ofNullable(state)
				.map(s -> s.toLowerCase(Locale.ROOT))
				.orElse("");
		return normalized.equals("succeeded") || normalized.equals("failed");
	}

	private static boolean isSucceededState(final String state) {
		return Optional.ofNullable(state)
				.map(s -> s.toLowerCase(Locale.ROOT))
				.filter("succeeded"::equals)
				.isPresent();
	}

	private Map<String, ?> createOrGetIntegrationServiceKeyCredentials(final String serviceInstanceId) {
		final Instant deadline = Instant.now().plus(SERVICE_KEY_RETRY_TIMEOUT);
		RuntimeException lastException = null;
		int attempt = 1;

		while (Instant.now().isBefore(deadline)) {
			try {
				final Optional<Map<String, Object>> existingCredentials = getIntegrationServiceKeyCredentials(serviceInstanceId);
				if (existingCredentials.isPresent()) {
					log.info("Reusing existing integration service key for service instance '{}'", serviceInstanceId);
					return existingCredentials.get();
				}

				final CreateServiceKeyResponse response = cfClient.serviceKeys().create(CreateServiceKeyRequest.builder()
								.name("integration-tests")
								.serviceInstanceId(serviceInstanceId)
								.build())
						.block();
				return response.getEntity().getCredentials();
			} catch (RuntimeException e) {
				lastException = e;
				if (!isRetryableServiceKeyCreationError(e)) {
					throw e;
				}

				log.warn("Service key creation attempt {} failed for service instance '{}' ({}: {}), retrying",
						attempt,
						serviceInstanceId,
						e.getClass().getSimpleName(),
						e.getMessage());
				attempt++;

				try {
					Thread.sleep(SERVICE_KEY_RETRY_DELAY.toMillis());
				} catch (InterruptedException interruptedException) {
					Thread.currentThread().interrupt();
					throw new IllegalStateException("Interrupted while retrying service key creation", interruptedException);
				}
			}
		}

		throw new IllegalStateException("Timed out while creating integration service key", lastException);
	}

	private static boolean isRetryableServiceKeyCreationError(final Throwable throwable) {
		final String details = collectThrowableMessages(throwable).toLowerCase(Locale.ROOT);
		return details.contains("operation in progress")
				|| details.contains("serviceinstanceoperationinprogress")
				|| details.contains("currently being updated")
				|| details.contains("temporary")
				|| details.contains("timed out")
				|| details.contains("timeout")
				|| details.contains("too many requests")
				|| details.contains("rate limit")
				|| details.contains("connection reset")
				|| details.contains("connection refused")
				|| details.contains("broken pipe")
				|| details.contains("gateway timeout")
				|| details.contains("bad gateway")
				|| details.contains("service unavailable");
	}

	private static String collectThrowableMessages(final Throwable throwable) {
		final StringBuilder details = new StringBuilder();
		Throwable current = throwable;
		while (current != null) {
			if (current.getMessage() != null) {
				details.append(current.getMessage()).append(' ');
			}
			current = current.getCause();
		}
		return details.toString();
	}

	private Optional<Map<String, Object>> getIntegrationServiceKeyCredentials(final String serviceInstanceId) {
		return cfClient.serviceKeys().list(ListServiceKeysRequest.builder()
						.name("integration-tests")
						.serviceInstanceId(serviceInstanceId)
						.build())
				.map(ListServiceKeysResponse::getResources)
				.flatMap(bindings -> bindings.size() > 0 ? Mono.just(bindings.get(0)) : Mono.empty())
				.map(ServiceKeyResource::getEntity)
				.map(ServiceKeyEntity::getCredentials)
				.blockOptional();
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

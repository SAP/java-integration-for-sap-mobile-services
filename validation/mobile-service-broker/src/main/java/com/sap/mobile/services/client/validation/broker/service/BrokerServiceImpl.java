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
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.v3.Metadata;
import org.cloudfoundry.client.v3.Relationship;
import org.cloudfoundry.client.v3.ToOneRelationship;
import org.cloudfoundry.client.v3.domains.GetDomainRequest;
import org.cloudfoundry.client.v3.routes.GetRouteRequest;
import org.cloudfoundry.client.v3.servicebindings.CreateServiceBindingRequest;
import org.cloudfoundry.client.v3.servicebindings.GetServiceBindingDetailsRequest;
import org.cloudfoundry.client.v3.servicebindings.GetServiceBindingDetailsResponse;
import org.cloudfoundry.client.v3.servicebindings.ListServiceBindingsRequest;
import org.cloudfoundry.client.v3.servicebindings.ListServiceBindingsResponse;
import org.cloudfoundry.client.v3.servicebindings.ServiceBindingRelationships;
import org.cloudfoundry.client.v3.servicebindings.ServiceBindingResource;
import org.cloudfoundry.client.v3.servicebindings.ServiceBindingType;
import org.cloudfoundry.client.v3.serviceinstances.CreateServiceInstanceRequest;
import org.cloudfoundry.client.v3.serviceinstances.ListServiceInstancesRequest;
import org.cloudfoundry.client.v3.serviceinstances.ListServiceInstancesResponse;
import org.cloudfoundry.client.v3.serviceinstances.ServiceInstanceRelationships;
import org.cloudfoundry.client.v3.serviceinstances.ServiceInstanceResource;
import org.cloudfoundry.client.v3.serviceinstances.ServiceInstanceType;
import org.cloudfoundry.client.v3.serviceinstances.UpdateServiceInstanceRequest;
import org.cloudfoundry.client.v3.serviceplans.ServicePlanResource;
import org.cloudfoundry.client.v3.spaces.SpaceResource;
import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
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
	private final ConnectionContext connectionContext;
	private final TokenProvider tokenProvider;
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

		cfClient.serviceInstancesV3()
				.create(CreateServiceInstanceRequest.builder()
						.type(ServiceInstanceType.MANAGED)
						.name(name)
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

		cfClient.serviceInstancesV3().update(UpdateServiceInstanceRequest.builder()
						.serviceInstanceId(instance.getId())
						.metadata(Metadata.builder()
								.labels(labels)
								.build())
						.build()
		).block();
		instance = waitForServiceInstanceCreation(name);

		final Map<String, ?> credentials = createServiceKey(instance.getId());

		cockpitClient.restoreApp(instance);

		return credentials;
	}

	private Map<String, Object> createServiceKey(final String serviceInstanceId) throws InstanceCreationTimeoutException, InstanceCreationFailedException {
		cfClient.serviceBindingsV3().create(CreateServiceBindingRequest.builder()
						.type(ServiceBindingType.KEY)
						.name("integration-tests")
						.relationships(ServiceBindingRelationships.builder()
								.serviceInstance(ToOneRelationship.builder()
										.data(Relationship.builder().id(serviceInstanceId).build())
										.build())
								.build())
						.build())
				.block();

		final ServiceBindingResource binding = waitForServiceKeyCreation(serviceInstanceId);

		return cfClient.serviceBindingsV3().getDetails(GetServiceBindingDetailsRequest.builder()
						.serviceBindingId(binding.getId())
						.build())
				.map(GetServiceBindingDetailsResponse::getCredentials)
				.block();
	}


	@Override
	public Map<String, ?> getMobileApplicationKey(final String appId) throws NoSuchServiceInstanceException {
		final ServiceInstanceResource instance = getMobileApplication(appId);

		return cfClient.serviceBindingsV3().list(ListServiceBindingsRequest.builder()
						.type(ServiceBindingType.KEY)
						.name("integration-tests")
						.serviceInstanceId(instance.getId())
						.build())
				.map(ListServiceBindingsResponse::getResources)
				.flatMap(bindings -> bindings.size() > 0 ? Mono.just(bindings.get(0)) : Mono.empty())
				.flatMap(binding -> cfClient.serviceBindingsV3().getDetails(GetServiceBindingDetailsRequest.builder()
						.serviceBindingId(binding.getId())
						.build()))
				.map(GetServiceBindingDetailsResponse::getCredentials)
				.blockOptional()
				.orElseGet(() -> {
					try {
						return createServiceKey(instance.getId());
					} catch (InstanceCreationTimeoutException | InstanceCreationFailedException e) {
						throw new IllegalStateException("Failed to create service key for instance " + appId, e);
					}
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

	private static final Set<String> NON_TERMINAL_OPERATION_STATES = Collections.unmodifiableSet(Sets.newHashSet("initial", "in progress"));

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
					}, (i -> !NON_TERMINAL_OPERATION_STATES.contains(i.getLastOperation().getState())));

			if (!instance.getLastOperation().getState().equals("succeeded")) {
				throw new InstanceCreationFailedException();
			}

			return instance;
		} catch (ConditionTimeoutException e) {
			throw new InstanceCreationTimeoutException();
		}
	}

	private ServiceBindingResource waitForServiceKeyCreation(final String serviceInstanceId) throws InstanceCreationTimeoutException, InstanceCreationFailedException {
		try {
			final ServiceBindingResource binding = Awaitility.await().atMost(Duration.ofMinutes(3))
					.with()
					.pollDelay(Duration.ofSeconds(5))
					.until(() -> {
						return cfClient.serviceBindingsV3().list(ListServiceBindingsRequest.builder()
										.type(ServiceBindingType.KEY)
										.name("integration-tests")
										.serviceInstanceId(serviceInstanceId)
										.build())
								.map(ListServiceBindingsResponse::getResources)
								.map(l -> l.get(0))
								.block();
					}, (b -> b.getLastOperation() == null || !NON_TERMINAL_OPERATION_STATES.contains(b.getLastOperation().getState())));

			if (binding.getLastOperation() != null && !"succeeded".equals(binding.getLastOperation().getState())) {
				throw new InstanceCreationFailedException();
			}

			return binding;
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
		return Optional.ofNullable(findRouteServiceBindingRouteId(appInstance.getId())
				.flatMap(routeId -> cfClient.routesV3().get(GetRouteRequest.builder().routeId(routeId).build()))
				.flatMap(route -> {
					return cfClient.domainsV3().get(GetDomainRequest.builder()
							.domainId(route.getRelationships().getDomain().getData().getId())
							.build()).map(domain -> {
						final String hostname = route.getHost() + "." + domain.getName();
						return UriComponentsBuilder.newInstance().scheme("https").host(hostname).path(route.getPath()).build().toUri();
					});
				}).block());
	}

	/**
	 * The CF Java client (5.17.0.RELEASE) does not expose a typed wrapper for the /v3/service_route_bindings
	 * endpoint, so it is called directly, reusing the same connection context (TLS/proxy settings) and token
	 * provider (OAuth token) that back the typed {@link CloudFoundryClient}.
	 */
	private Mono<String> findRouteServiceBindingRouteId(final String serviceInstanceId) {
		return connectionContext.getRootProvider().getRoot(connectionContext)
				.zipWith(tokenProvider.getToken(connectionContext))
				.flatMap(rootAndToken -> connectionContext.getHttpClient()
						.headers(headers -> headers.add("Authorization", "bearer " + rootAndToken.getT2()))
						.get()
						.uri(rootAndToken.getT1() + "v3/service_route_bindings?service_instance_guids=" + serviceInstanceId)
						.responseSingle((response, body) -> body.asString()))
				.map(objectMapper::readTree)
				.mapNotNull(json -> {
					final JsonNode routeId = json.at("/resources/0/relationships/route/data/guid");
					return routeId.isMissingNode() ? null : routeId.asText();
				});
	}
}

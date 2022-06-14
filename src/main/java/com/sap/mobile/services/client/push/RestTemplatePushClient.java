package com.sap.mobile.services.client.push;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.AccessLevel;
import lombok.Getter;

class RestTemplatePushClient implements PushClient {

	@Getter(AccessLevel.PACKAGE)
	private final RestTemplate restTemplate;
	private final ClientConfiguration config;

	/**
	 * Construct a push client that is based on a rest template.
	 *
	 * @param config configuration object for the client
	 */
	RestTemplatePushClient(ClientConfiguration config) {
		Assert.notNull(config, "Config must not be null.");

		RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
		restTemplateBuilder = Optional.ofNullable(config.getConnectTimeout())
				.map(restTemplateBuilder::setConnectTimeout).orElse(restTemplateBuilder);
		restTemplateBuilder = Optional.ofNullable(config.getReadTimeout()).map(restTemplateBuilder::setReadTimeout)
				.orElse(restTemplateBuilder);
		restTemplateBuilder = restTemplateBuilder
				.additionalInterceptors(new ClientInfoRequestInterceptor(BuildProperties.getInstance()));
		restTemplateBuilder = restTemplateBuilder.additionalInterceptors(config.getAuthInterceptor());
		restTemplateBuilder = restTemplateBuilder.additionalInterceptors(new ApiWarnHeaderRequestInterceptor());
		restTemplateBuilder = restTemplateBuilder.rootUri(config.getRootUri());
		restTemplateBuilder = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler());
		this.restTemplate = restTemplateBuilder.build();
		this.config = config;
	}

	@Override
	public PushResponse pushToApplication(final PushPayload pushPayload) throws ClientException {
		DTOPushPayload payload = new DTOPushPayload(pushPayload);
		RequestEntity<DTOPushPayload> request =
				RequestEntity.method(HttpMethod.POST, Constants.Paths.Backend.V1.PUSH_TO_APPLICATION_PATH,
								this.basicPathVariables().build())
						.headers(this.basicHeaders())
						.body(payload);
		ResponseEntity<DTOPushResponse> response = this.restTemplate.exchange(request, DTOPushResponse.class);
		return response.getBody();
	}

	@Override
	public PushResponse pushToApplication(LocalizedPushPayload pushPayload) throws ClientException {
		DTOLocalizedPushPayload payload = new DTOLocalizedPushPayload(pushPayload);
		RequestEntity<DTOLocalizedPushPayload> request =
				RequestEntity.method(HttpMethod.POST, Constants.Paths.Backend.V2.PUSH_TO_APPLICATION_PATH,
								this.basicPathVariables().build())
						.headers(this.basicHeaders())
						.body(payload);
		ResponseEntity<DTOPushResponse> response = this.restTemplate.exchange(request, DTOPushResponse.class);
		return response.getBody();
	}

	@Override
	public PushResponse pushToDevice(final String userId, final String deviceId, final PushPayload pushPayload) throws
			ClientException {
		DTOPushPayload payload = new DTOPushPayload(pushPayload);
		RequestEntity<DTOPushPayload> request =
				RequestEntity.method(HttpMethod.POST, Constants.Paths.Backend.V1.PUSH_TO_USER_PATH,
								this.basicPathVariables().put("userId", userId).putAndBuild("deviceId", deviceId))
						.headers(this.basicHeaders())
						.body(payload);
		ResponseEntity<DTOPushResponse> response = this.restTemplate.exchange(request, DTOPushResponse.class);
		return response.getBody();
	}

	@Override
	public PushResponse pushToDevice(String userId, String deviceId, LocalizedPushPayload pushPayload)
			throws ClientException {
		DTOLocalizedPushPayload payload = new DTOLocalizedPushPayload(pushPayload);
		RequestEntity<DTOLocalizedPushPayload> request =
				RequestEntity.method(HttpMethod.POST, Constants.Paths.Backend.V2.PUSH_TO_USER_PATH,
								this.basicPathVariables().put("userId", userId).putAndBuild("deviceId", deviceId))
						.headers(this.basicHeaders())
						.body(payload);
		ResponseEntity<DTOPushResponse> response = this.restTemplate.exchange(request, DTOPushResponse.class);
		return response.getBody();
	}

	@Override
	public PushResponse pushToUsers(final Collection<String> userIds, final PushPayload pushPayload) throws
			ClientException {
		DTOPushToUsersPayload payload = new DTOPushToUsersPayload(new ArrayList<>(userIds), pushPayload);
		RequestEntity<DTOPushToUsersPayload> request =
				RequestEntity.method(HttpMethod.POST, Constants.Paths.Backend.V1.PUSH_TO_USERS_PATH,
								this.basicPathVariables().build())
						.headers(this.basicHeaders())
						.body(payload);
		ResponseEntity<DTOPushResponse> response = this.restTemplate.exchange(request, DTOPushResponse.class);
		return response.getBody();
	}

	@Override
	public PushResponse pushToUsers(Collection<String> userIds, LocalizedPushPayload pushPayload)
			throws ClientException {
		DTOLocalizedPushToUsersPayload payload =
				new DTOLocalizedPushToUsersPayload(new ArrayList<>(userIds), pushPayload);
		RequestEntity<DTOLocalizedPushToUsersPayload> request =
				RequestEntity.method(HttpMethod.POST, Constants.Paths.Backend.V2.PUSH_TO_USERS_PATH,
								this.basicPathVariables().build())
						.headers(this.basicHeaders())
						.body(payload);
		ResponseEntity<DTOPushResponse> response = this.restTemplate.exchange(request, DTOPushResponse.class);
		return response.getBody();
	}

	@Override
	public PushResponse pushToGroup(final String group, final PushPayload pushPayload) throws ClientException {
		DTOPushPayload payload = new DTOPushPayload(pushPayload);
		RequestEntity<DTOPushPayload> request =
				RequestEntity.method(HttpMethod.POST, Constants.Paths.Backend.V1.PUSH_TO_GROUP_PATH,
								this.basicPathVariables().putAndBuild("group", group))
						.headers(this.basicHeaders())
						.body(payload);
		ResponseEntity<DTOPushResponse> response = this.restTemplate.exchange(request, DTOPushResponse.class);
		return response.getBody();
	}

	@Override
	public PushResponse pushToGroup(String group, LocalizedPushPayload pushPayload) throws ClientException {
		DTOLocalizedPushPayload payload = new DTOLocalizedPushPayload(pushPayload);
		RequestEntity<DTOLocalizedPushPayload> request =
				RequestEntity.method(HttpMethod.POST, Constants.Paths.Backend.V2.PUSH_TO_GROUP_PATH,
								this.basicPathVariables().putAndBuild("group", group))
						.headers(this.basicHeaders())
						.body(payload);
		ResponseEntity<DTOPushResponse> response = this.restTemplate.exchange(request, DTOPushResponse.class);
		return response.getBody();
	}

	@Override
	public PushResponse pushToCapability(final String capability,
			final PushToCapabilitiesPayload pushToCapabilitiesPayload) throws
			ClientException {
		DTOPushToCapabilitiesPayload payload = new DTOPushToCapabilitiesPayload(pushToCapabilitiesPayload);
		RequestEntity<DTOPushToCapabilitiesPayload> request =
				RequestEntity.method(HttpMethod.POST, Constants.Paths.Backend.V1.PUSH_TO_CAPABILITY_PATH,
								this.basicPathVariables().putAndBuild("capabilityName", capability))
						.headers(this.basicHeaders())
						.body(payload);
		ResponseEntity<DTOPushResponse> response = this.restTemplate.exchange(request, DTOPushResponse.class);
		return response.getBody();
	}

	@Override
	public PushResponse pushToCapability(String capability,
			LocalizedPushToCapabilitiesPayload pushToCapabilitiesPayload) throws ClientException {
		DTOLocalizedPushToCapabilitiesPayload payload =
				new DTOLocalizedPushToCapabilitiesPayload(pushToCapabilitiesPayload);
		RequestEntity<DTOLocalizedPushToCapabilitiesPayload> request =
				RequestEntity.method(HttpMethod.POST, Constants.Paths.Backend.V2.PUSH_TO_CAPABILITY_PATH,
								this.basicPathVariables().putAndBuild("capabilityName", capability))
						.headers(this.basicHeaders())
						.body(payload);
		ResponseEntity<DTOPushResponse> response = this.restTemplate.exchange(request, DTOPushResponse.class);
		return response.getBody();
	}

	@Override
	public PushResponse bulkPush(final PushPayload rootNotification,
			final Collection<UserNotification> userNotifications) {
		DTOBulkPushPayload payload = new DTOBulkPushPayload(rootNotification, userNotifications);
		RequestEntity<DTOBulkPushPayload> request =
				RequestEntity.method(HttpMethod.POST, Constants.Paths.Backend.V1.BULK_PUSH_PATH,
								this.basicPathVariables().build())
						.headers(this.basicHeaders())
						.body(payload);
		ResponseEntity<DTOPushResponse> response = this.restTemplate.exchange(request, DTOPushResponse.class);
		return response.getBody();
	}

	@Override
	public PushResponse bulkPush(LocalizedPushPayload rootNotification,
			Collection<LocalizedUserNotification> userNotifications) {
		DTOLocalizedBulkPush payload = new DTOLocalizedBulkPush(rootNotification, userNotifications);
		RequestEntity<DTOLocalizedBulkPush> request =
				RequestEntity.method(HttpMethod.POST, Constants.Paths.Backend.V1.BULK_PUSH_PATH,
								this.basicPathVariables().build())
						.headers(this.basicHeaders())
						.body(payload);
		ResponseEntity<DTOPushResponse> response = this.restTemplate.exchange(request, DTOPushResponse.class);
		return response.getBody();
	}

	@Override
	public NotificationStatusResponse getNotificationStatus(final String notificationId) {
		RequestEntity<Void> request =
				RequestEntity.method(HttpMethod.GET, Constants.Paths.Backend.V1.GET_NOTIFICATION_STATUS,
								this.basicPathVariables().putAndBuild("notificationId", notificationId))
						.headers(this.basicHeaders())
						.build();
		ResponseEntity<DTONotificationStatusResponse> response =
				this.restTemplate.exchange(request, DTONotificationStatusResponse.class);
		return response.getBody();
	}

	@Override
	public Set<String> getLocalizations(Collection<String> userIds) {
		RequestEntity<Void> request = RequestEntity.method(HttpMethod.GET,
				UriComponentsBuilder.fromPath(Constants.Paths.Backend.V2.GET_LOCALIZATIONS)
						.queryParam(Constants.Params.Backend.V2.GET_LOCALIZATIONS_USER_PARAM, userIds)
						.build(this.basicPathVariables().build()).toString()).headers(this.basicHeaders()).build();
		ParameterizedTypeReference<Set<String>> type = new ParameterizedTypeReference<Set<String>>() {
		};
		ResponseEntity<Set<String>> response = this.restTemplate.exchange(request, type);
		return response.getBody();
	}

	private MapBuilder<String> basicPathVariables() {
		return MapBuilder.newInstance(String.class).put("applicationId", this.config.getApplicationId());
	}

	private HttpHeaders basicHeaders() {
		HttpHeaders headers = new HttpHeaders();
		this.config.getTenantResolver().get().ifPresent(tenantId -> {
			headers.add(Constants.Headers.TENANT_ID_HEADER, tenantId);
		});
		return headers;
	}
}

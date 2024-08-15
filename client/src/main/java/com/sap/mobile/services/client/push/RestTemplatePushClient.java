package com.sap.mobile.services.client.push;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.sap.mobile.services.client.ClientBuilderUtils;
import com.sap.mobile.services.client.ClientConfiguration;
import com.sap.mobile.services.client.ClientException;

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
		restTemplateBuilder = ClientBuilderUtils.addBasicConfiguration(restTemplateBuilder, config);
		restTemplateBuilder = ClientBuilderUtils.addDefaultInterceptors(restTemplateBuilder, config);
		restTemplateBuilder = restTemplateBuilder.errorHandler(new PushRestTemplateResponseErrorHandler());
		this.restTemplate = restTemplateBuilder.build();
		this.config = config;
	}

	@Override
	public PushResponse pushToApplication(final PushPayload pushPayload) throws ClientException {
		return pushToApplication(LocalizedPushPayload.builder().notification(pushPayload).build());
	}

	@Override
	public PushResponse pushToApplication(LocalizedPushPayload pushPayload) throws ClientException {
		DTOLocalizedPushPayload payload = new DTOLocalizedPushPayload(pushPayload);
		RequestEntity<DTOLocalizedPushPayload> request = RequestEntity
				.method(HttpMethod.POST, Constants.Backend.V2.Paths.PUSH_TO_APPLICATION_PATH,
						this.basicPathVariables().build())
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
				.body(payload);
		ResponseEntity<DTOPushResponse> response = this.restTemplate.exchange(request, DTOPushResponse.class);
		return response.getBody();
	}

	@Override
	public PushResponse pushToDevice(final String userId, final String deviceId, final PushPayload pushPayload)
			throws ClientException {
		return pushToDevice(userId, deviceId, LocalizedPushPayload.builder().notification(pushPayload).build());
	}

	@Override
	public PushResponse pushToDevice(String userId, String deviceId, LocalizedPushPayload pushPayload)
			throws ClientException {
		DTOLocalizedPushPayload payload = new DTOLocalizedPushPayload(pushPayload);
		RequestEntity<DTOLocalizedPushPayload> request = RequestEntity
				.method(HttpMethod.POST, Constants.Backend.V2.Paths.PUSH_TO_USER_PATH,
						this.basicPathVariables().put("userId", userId).putAndBuild("deviceId", deviceId))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
				.body(payload);
		ResponseEntity<DTOPushResponse> response = this.restTemplate.exchange(request, DTOPushResponse.class);
		return response.getBody();
	}

	@Override
	public PushResponse pushToUsers(final Collection<String> userIds, final PushPayload pushPayload)
			throws ClientException {
		return pushToUsers(userIds, LocalizedPushPayload.builder().notification(pushPayload).build());
	}

	@Override
	public PushResponse pushToUsers(Collection<String> userIds, Collection<String> userUUIDs,
			LocalizedPushPayload pushPayload) throws ClientException {
		DTOLocalizedPushToUsersPayload payload = new DTOLocalizedPushToUsersPayload(
				CollectionUtils.isEmpty(userIds) ? null : new ArrayList<>(userIds),
				CollectionUtils.isEmpty(userUUIDs) ? null : new ArrayList<>(userUUIDs),
				pushPayload);
		RequestEntity<DTOLocalizedPushToUsersPayload> request = RequestEntity
				.method(HttpMethod.POST, Constants.Backend.V2.Paths.PUSH_TO_USERS_PATH,
						this.basicPathVariables().build())
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
				.body(payload);
		ResponseEntity<DTOPushResponse> response = this.restTemplate.exchange(request, DTOPushResponse.class);
		return response.getBody();
	}

	@Override
	public PushResponse pushToUsers(Collection<String> userIds, LocalizedPushPayload pushPayload)
			throws ClientException {
		return pushToUsers(userIds, null, pushPayload);
	}

	@Override
	public PushResponse pushToGroup(final String group, final PushPayload pushPayload) throws ClientException {
		return pushToGroup(group, LocalizedPushPayload.builder().notification(pushPayload).build());
	}

	@Override
	public PushResponse pushToGroup(String group, LocalizedPushPayload pushPayload) throws ClientException {
		DTOLocalizedPushPayload payload = new DTOLocalizedPushPayload(pushPayload);
		RequestEntity<DTOLocalizedPushPayload> request = RequestEntity
				.method(HttpMethod.POST, Constants.Backend.V2.Paths.PUSH_TO_GROUP_PATH,
						this.basicPathVariables().putAndBuild("group", group))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
				.body(payload);
		ResponseEntity<DTOPushResponse> response = this.restTemplate.exchange(request, DTOPushResponse.class);
		return response.getBody();
	}

	@Override
	public PushResponse pushToCapability(final String capability,
			final PushToCapabilitiesPayload pushToCapabilitiesPayload) throws ClientException {
		return pushToCapability(capability, LocalizedPushToCapabilitiesPayload.builder().notification(
				LocalizedPushPayload.builder().notification(pushToCapabilitiesPayload.getNotification()).build())
				.users(pushToCapabilitiesPayload.getCapabilityUsers())
				.build());
	}

	@Override
	public PushResponse pushToCapability(String capability,
			LocalizedPushToCapabilitiesPayload pushToCapabilitiesPayload) throws ClientException {
		DTOLocalizedPushToCapabilitiesPayload payload = new DTOLocalizedPushToCapabilitiesPayload(
				pushToCapabilitiesPayload);
		RequestEntity<DTOLocalizedPushToCapabilitiesPayload> request = RequestEntity
				.method(HttpMethod.POST, Constants.Backend.V2.Paths.PUSH_TO_CAPABILITY_PATH,
						this.basicPathVariables().putAndBuild("capabilityName", capability))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
				.body(payload);
		ResponseEntity<DTOPushResponse> response = this.restTemplate.exchange(request, DTOPushResponse.class);
		return response.getBody();
	}

	@Override
	public PushResponse pushToTopics(Collection<String> userIds, Collection<String> userUUIDs,
			Collection<String> topics, LocalizedPushPayload pushPayload) {
		DTOLocalizedPushToTopicPayload payload = new DTOLocalizedPushToTopicPayload(
				CollectionUtils.isEmpty(userIds) ? null : new ArrayList<>(userIds),
				CollectionUtils.isEmpty(userUUIDs) ? null : new ArrayList<>(userUUIDs),
				new ArrayList<>(topics), pushPayload);
		RequestEntity<DTOLocalizedPushToTopicPayload> request = RequestEntity
				.method(HttpMethod.POST, Constants.Backend.V2.Paths.PUSH_TO_TOPIC_PATH,
						this.basicPathVariables().build())
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
				.body(payload);
		ResponseEntity<DTOPushResponse> response = this.restTemplate.exchange(request, DTOPushResponse.class);
		return response.getBody();
	}

	@Override
	public PushResponse pushToTopics(Collection<String> userIds, Collection<String> topics,
			LocalizedPushPayload pushPayload) {
		return pushToTopics(userIds, null, topics, pushPayload);
	}

	@Override
	public PushResponse bulkPush(final PushPayload rootNotification,
			final Collection<UserNotification> userNotifications) {
		return bulkPush(LocalizedPushPayload.builder().notification(rootNotification).build(),
				userNotifications.stream().map(un -> LocalizedUserNotification.builder().user(un.getUser())
						.notification(LocalizedPushPayload.builder().notification(un.getNotification()).build())
						.build()).collect(Collectors.toList()));
	}

	@Override
	public PushResponse bulkPush(LocalizedPushPayload rootNotification,
			Collection<LocalizedUserNotification> userNotifications) {
		DTOLocalizedBulkPush payload = new DTOLocalizedBulkPush(rootNotification, userNotifications);
		RequestEntity<DTOLocalizedBulkPush> request = RequestEntity
				.method(HttpMethod.POST, Constants.Backend.V2.Paths.BULK_PUSH_PATH,
						this.basicPathVariables().build())
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
				.body(payload);
		ResponseEntity<DTOPushResponse> response = this.restTemplate.exchange(request, DTOPushResponse.class);
		return response.getBody();
	}

	@Override
	public NotificationStatusResponse getNotificationStatus(final String notificationId) {
		RequestEntity<Void> request = RequestEntity
				.method(HttpMethod.GET, Constants.Backend.V2.Paths.GET_NOTIFICATION_STATUS,
						this.basicPathVariables().putAndBuild("notificationId", notificationId))
				.build();
		ResponseEntity<DTONotificationStatusResponse> response = this.restTemplate.exchange(request,
				DTONotificationStatusResponse.class);
		return response.getBody();
	}

	@Override
	public Set<String> getLocalizations(Collection<String> userIds) {
		RequestEntity<Void> request = RequestEntity.method(HttpMethod.GET,
				UriComponentsBuilder.fromPath(Constants.Backend.V2.Paths.GET_LOCALIZATIONS)
						.queryParam(Constants.Backend.V2.Params.GET_LOCALIZATIONS_USER_PARAM, userIds)
						.build(this.basicPathVariables().build()).toString())
				.build();
		ParameterizedTypeReference<Set<String>> type = new ParameterizedTypeReference<Set<String>>() {
		};
		ResponseEntity<Set<String>> response = this.restTemplate.exchange(request, type);
		return response.getBody();
	}

	@Override
	public PushRegistrationsResponse getRegistrations(Optional<String> usernameOpt, Optional<String> groupOpt) {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
				.fromPath(Constants.Backend.V2.Paths.GET_REGISTRATIONS);
		if (usernameOpt.isPresent()) {
			uriComponentsBuilder = uriComponentsBuilder.queryParam(
					Constants.Backend.V2.Params.GET_REGISTRATIONS_USERNAME_PARAM,
					usernameOpt.get());
		}
		if (groupOpt.isPresent()) {
			uriComponentsBuilder = uriComponentsBuilder.queryParam(
					Constants.Backend.V2.Params.GET_REGISTRATIONS_GROUP_PARAM,
					groupOpt.get());
		}

		RequestEntity<Void> request = RequestEntity.method(HttpMethod.GET,
				uriComponentsBuilder.build(this.basicPathVariables().build()).toString()).build();
		ResponseEntity<DTOPushRegistrationsResponse> response = this.restTemplate.exchange(request,
				DTOPushRegistrationsResponse.class);
		return response.getBody();
	}

	@Override
	public Optional<PushRegistration> getRegistration(String registrationId) {
		RequestEntity<Void> request = RequestEntity.method(HttpMethod.GET,
				Constants.Backend.V2.Paths.GET_REGISTRATION,
				this.basicPathVariables().putAndBuild("registrationId", registrationId)).build();
		try {
			ResponseEntity<DTOPushRegistration> response = this.restTemplate.exchange(request,
					DTOPushRegistration.class);
			return Optional.ofNullable(response.getBody());
		} catch (NoMessageSentException nme) {
			// expected when no registration was found
			return Optional.empty();
		}
	}

	private MapBuilder<String> basicPathVariables() {
		return MapBuilder.newInstance(String.class).put("applicationId", this.config.getApplicationId());
	}
}

package com.sap.mobile.services.client.push.mock;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.RequestMatcher;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.sap.mobile.services.client.MobileServicesBinding;
import com.sap.mobile.services.client.MobileServicesSettings;

public class MockPushServer {

	private final String baseUrl;
	private final String applicationId;
	private final String apiKey;
	private final MockRestServiceServer mockServer;

	public MockPushServer(final RestTemplate restTemplate, final MobileServicesSettings settings) {
		this.mockServer = MockRestServiceServer.createServer(restTemplate);
		this.baseUrl = settings.getServices().stream().filter(s -> s.getName().equals("push")).findFirst().get()
				.getServiceKeys().get(0).getUrl();
		this.apiKey = settings.getServices().stream().filter(s -> s.getName().equals("push")).findFirst().get()
				.getServiceKeys().get(0).getApiKey();
		this.applicationId = settings.getApplicationId();
	}

	public MockPushServer(final RestTemplate restTemplate, final MobileServicesBinding binding) {
		this.mockServer = MockRestServiceServer.createServer(restTemplate);
		this.baseUrl = binding.getEndpoints().get("mobileservices").getUrl();
		this.applicationId = binding.getAppName();
		this.apiKey = null;
	}

	public static Matcher<MockPushServer> hasBeenVerified() {
		return new TypeSafeMatcher<MockPushServer>() {
			@Override
			public void describeTo(final Description description) {
			}

			@Override
			protected boolean matchesSafely(final MockPushServer item) {
				item.verify();
				return true;
			}
		};
	}

	public RequestExpectation<PushToApplicationResponseDefinition> expectPushToApplication() {
		final String expectedUri = UriComponentsBuilder.fromUriString(baseUrl)
				.path("/mobileservices/push/v1/backend/applications/{applicationId}/notifications")
				.buildAndExpand(applicationId).toString();

		final PushToApplicationResponseDefinition responseDefinition = new PushToApplicationResponseDefinition(this);
		final RequestExpectation<PushToApplicationResponseDefinition> expectation =
				new RequestExpectation<>(this, responseDefinition);

		return expectPushWithUrl(expectedUri, expectation);
	}

	public RequestExpectation<PushToUserResponseDefinition> expectPushToUser(final String userId,
			final String deviceId) {
		final String expectedUri = UriComponentsBuilder.fromUriString(baseUrl)
				.path("/mobileservices/push/v1/backend/applications/{applicationId}/users/{userId}/devices/{deviceId}/notifications")
				.buildAndExpand(applicationId, userId, deviceId).toString();

		final PushToUserResponseDefinition responseDefinition = new PushToUserResponseDefinition(this);
		final RequestExpectation<PushToUserResponseDefinition> expectation =
				new RequestExpectation<>(this, responseDefinition);

		return expectPushWithUrl(expectedUri, expectation);
	}

	public RequestExpectation<PushToUsersResponseDefinition> expectPushToUsers() {
		final String expectedUri = UriComponentsBuilder.fromUriString(baseUrl)
				.path("/mobileservices/push/v1/backend/applications/{applicationId}/notifications/users")
				.buildAndExpand(applicationId).toString();

		final PushToUsersResponseDefinition responseDefinition = new PushToUsersResponseDefinition(this);
		final RequestExpectation<PushToUsersResponseDefinition> expectation =
				new RequestExpectation<>(this, responseDefinition);

		return expectPushWithUrl(expectedUri, expectation);
	}

	private <T extends RequestExpectation<?>> T expectPushWithUrl(final String expectedUri, final T expectation) {
		mockServer.expect(requestTo(expectedUri))
				.andExpect(method(HttpMethod.POST))
				.andExpect(req -> {
					for (final RequestMatcher matcher : expectation.getAdditionalMatchers()) {
						matcher.match(req);
					}
				})
				.andRespond((req) -> {
					return withStatus(expectation.getResponseDefinition().getStatus())
							.contentType(MediaType.APPLICATION_JSON)
							.body(expectation.getResponseDefinition().getBody())
							.createResponse(req);
				});

		if (apiKey != null) {
			expectation.withServiceKeyAuth(apiKey);
		}

		return expectation;
	}

	public void verify() {
		mockServer.verify();
	}

	boolean isApiKeyAuth() {
		return this.apiKey != null;
	}

}

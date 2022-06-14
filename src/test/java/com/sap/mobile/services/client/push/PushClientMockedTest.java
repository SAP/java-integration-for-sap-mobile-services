/**
 * (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * No part of this publication may be reproduced or transmitted in any form or for any purpose
 * without the express permission of SAP SE. The information contained herein may be changed
 * without prior notice.
 */

package com.sap.mobile.services.client.push;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.sap.mobile.services.client.push.mock.MockPushServer;
import com.sap.cloud.security.xsuaa.client.OAuth2TokenResponse;
import com.sap.cloud.security.xsuaa.tokenflows.ClientCredentialsTokenFlow;

public class PushClientMockedTest {

	private static final String DEFAULT_USER_ID = "john.doe@example.com";
	private static final String DEFAULT_DEVICE_ID = "25faa19e-1b70-429c-8e24-9c9dd363e5b7";

	private RestTemplatePushClient testee;
	private MockPushServer mockPushServer;

	@Before
	public void setUp() throws Exception {
		final MobileServicesSettings settings = MobileServicesSettings.fromResource("mobileservices-config.json");
		this.testee = (RestTemplatePushClient) new PushClientBuilder()
				.build(settings);
		this.mockPushServer = new MockPushServer(testee.getRestTemplate(), settings);
	}

	@Test
	public void testPushToApplication() throws Exception {
		mockPushServer.expectPushToApplication()
				.withJsonBodyFromResource("payloads/request-push-to-application-alert.json");

		testee.pushToApplication(PushPayload.builder()
				.alert("Hello World")
				.build());

		MatcherAssert.assertThat(mockPushServer, MockPushServer.hasBeenVerified());
	}

	@Test
	public void testPushToApplicationDeviceSpecific() throws Exception {
		mockPushServer.expectPushToApplication()
				.withJsonBodyFromResource("payloads/request-push-to-application-device-specific.json");

		testee.pushToApplication(PushPayload.builder()
				.alert("Hello World")
				.gcm(GcmNotification.builder()
						.title("Hello World from GCM")
						.body("Sent by the CPMS Push Library")
						.build())
				.apns(ApnsNotification.builder()
						.alertTitle("Hello World from APNS")
						.alertSubtitle("With some subtitle")
						.alertBody("Sent by the CPMS Push Library")
						.build())
				.build());

		MatcherAssert.assertThat(mockPushServer, MockPushServer.hasBeenVerified());
	}

	@Test
	public void testPushToUser() throws Exception {
		mockPushServer.expectPushToUser(DEFAULT_USER_ID, DEFAULT_DEVICE_ID)
				.withJsonBodyFromResource("payloads/request-push-to-user-alert.json");

		final PushResponse response = testee.pushToDevice(DEFAULT_USER_ID, DEFAULT_DEVICE_ID, PushPayload.builder()
				.alert("Hello World")
				.build());

		assertThat(response.getStatus(), is(nullValue()));

		assertThat(response.getResults(), hasSize(1));
		assertThat(response.getResults().get(0).getCode(), is(0));
		assertThat(response.getResults().get(0).getMessage(), is("success"));
		assertThat(response.getResults().get(0).getNotificationId(), is(not(emptyOrNullString())));
		assertThat(response.getResults().get(0).getTarget(), is(not(emptyOrNullString())));
		MatcherAssert.assertThat(mockPushServer, MockPushServer.hasBeenVerified());
	}

	@Test
	public void testPushToUserNoSuchRegistration() throws Exception {
		mockPushServer.expectPushToUser(DEFAULT_USER_ID, DEFAULT_DEVICE_ID)
				.andRespond().withNoSuchRegistrationError();

		final PushResponse response = testee.pushToDevice(DEFAULT_USER_ID, DEFAULT_DEVICE_ID, PushPayload.builder()
				.alert("Hello World")
				.build());

		assertThat(response.getStatus().getCode(), is(400));
		assertThat(response.getStatus().getMessage(), is("no registrations for given user names found"));
		MatcherAssert.assertThat(mockPushServer, MockPushServer.hasBeenVerified());
	}

	@Test
	public void testPushToUsers() throws Exception {
		mockPushServer.expectPushToUsers()
				.withJsonBodyFromResource("payloads/request-push-to-users-alert.json");

		final PushResponse response = testee.pushToUsers(Arrays.asList("john.doe@example.com", "jane.doe@example.com"),
				PushPayload.builder()
						.alert("Hello World")
						.build());

		//TODO: check why status = null
		//assertThat(response.getStatus().getCode(), is(0));

		assertThat(response.getResults(), hasSize(2));
		assertThat(response.getResults().get(0).getCode(), is(0));
		assertThat(response.getResults().get(0).getMessage(), is("success"));
		assertThat(response.getResults().get(0).getNotificationId(), is(not(emptyOrNullString())));
		assertThat(response.getResults().get(0).getTarget(), is(not(emptyOrNullString())));
		assertThat(response.getResults().get(1).getCode(), is(0));
		MatcherAssert.assertThat(mockPushServer, MockPushServer.hasBeenVerified());
	}

	@Test
	public void testPushToUsersWithEmptyList() throws Exception {
		mockPushServer.expectPushToUsers()
				.withJsonBodyFromResource("payloads/request-push-to-users-empty-list.json")
				.andRespond().withInvalidPushUserListSizeError();

		assertThrows(Exception.class, () -> {
			//TODO: enhance error handling
			testee.pushToUsers(Collections.emptyList(), PushPayload.builder()
					.alert("Hello World")
					.build());
		});

		MatcherAssert.assertThat(mockPushServer, MockPushServer.hasBeenVerified());
	}

	@Test
	public void testPushToUsersNoSuchRegistration() throws Exception {
		mockPushServer.expectPushToUsers()
				.withJsonBodyFromResource("payloads/request-push-to-users-alert.json")
				.andRespond().withNoSuchRegistrationError();

		final PushResponse response = testee.pushToUsers(Arrays.asList("john.doe@example.com", "jane.doe@example.com"),
				PushPayload.builder()
						.alert("Hello World")
						.build());

		assertThat(response.getStatus().getCode(), is(400));
		assertThat(response.getStatus().getMessage(), is("no registrations for given user names found"));
		MatcherAssert.assertThat(mockPushServer, MockPushServer.hasBeenVerified());
	}

	@Test
	public void testWithServiceBinding() throws Exception {
		final MobileServicesBinding binding = MobileServicesBinding.fromResource("mobileservices-binding.json");
		final XsuaaTokenFlowFactory factory = Mockito.mock(XsuaaTokenFlowFactory.class);
		final ClientCredentialsTokenFlow flow = Mockito.mock(ClientCredentialsTokenFlow.class);

		Mockito.when(factory.createClientCredentialsTokenFlow(binding)).thenReturn(flow);
		this.testee = (RestTemplatePushClient) new PushClientBuilder()
				.withTokenFlowFactory(factory)
				.build(binding);
		this.mockPushServer = new MockPushServer(testee.getRestTemplate(), binding);
		mockPushServer.expectPushToApplication()
				.withJsonBodyFromResource("payloads/request-push-to-application-alert.json")
				.withBearerToken("my-secret-jwt");

		final OAuth2TokenResponse response = new OAuth2TokenResponse("my-secret-jwt", 60, "refresh-me");
		Mockito.when(flow.execute()).thenReturn(response);

		testee.pushToApplication(PushPayload.builder()
				.alert("Hello World")
				.build());

		MatcherAssert.assertThat(mockPushServer, MockPushServer.hasBeenVerified());
	}

	@Test
	public void testWithServiceKeyAndTenantResolver() throws Exception {
		final String tenantZoneId = UUID.randomUUID().toString();
		final MobileServicesSettings settings = MobileServicesSettings.fromResource("mobileservices-config.json");
		this.testee = (RestTemplatePushClient) new PushClientBuilder()
				.withTenantResolver(() -> Optional.of(tenantZoneId))
				.build(settings);
		this.mockPushServer = new MockPushServer(testee.getRestTemplate(), settings);

		mockPushServer.expectPushToApplication()
				.withJsonBodyFromResource("payloads/request-push-to-application-alert.json")
				.forTenant(tenantZoneId);

		testee.pushToApplication(PushPayload.builder()
				.alert("Hello World")
				.build());

		MatcherAssert.assertThat(mockPushServer, MockPushServer.hasBeenVerified());
	}

	@Test
	public void testWithServiceBindingAndTenantResolver() throws Exception {
		final MobileServicesBinding binding = MobileServicesBinding.fromResource("mobileservices-binding.json");
		final XsuaaTokenFlowFactory factory = Mockito.mock(XsuaaTokenFlowFactory.class);
		final ClientCredentialsTokenFlow flow = Mockito.mock(ClientCredentialsTokenFlow.class);

		Mockito.when(factory.createClientCredentialsTokenFlow(binding)).thenReturn(flow);
		this.testee = (RestTemplatePushClient) new PushClientBuilder()
				.withTokenFlowFactory(factory)
				.withTenantResolver(() -> Optional.of("49ed9cfd-1e52-431e-8a9a-5dbe880ab9fb"))
				.build(binding);
		this.mockPushServer = new MockPushServer(testee.getRestTemplate(), binding);
		mockPushServer.expectPushToApplication()
				.withJsonBodyFromResource("payloads/request-push-to-application-alert.json")
				.withBearerToken("my-secret-jwt");

		final OAuth2TokenResponse response = new OAuth2TokenResponse("my-secret-jwt", 60, "refresh-me");
		Mockito.when(flow.zoneId("49ed9cfd-1e52-431e-8a9a-5dbe880ab9fb")).thenReturn(flow);
		Mockito.when(flow.execute()).thenReturn(response);

		testee.pushToApplication(PushPayload.builder()
				.alert("Hello World")
				.build());

		MatcherAssert.assertThat(mockPushServer, MockPushServer.hasBeenVerified());
		Mockito.verify(flow).zoneId("49ed9cfd-1e52-431e-8a9a-5dbe880ab9fb");
	}
}

package com.sap.cloud.platform.mobile.services.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sap.cloud.platform.mobile.services.validation.config.MobileServicesClientConfiguration;
import com.sap.mobile.services.client.push.PushPayload;
import com.sap.mobile.services.client.push.PushResponse;

@SpringBootTest(classes = {
		MobileServicesClientConfiguration.class
})
public class PushClientTest {

	@Autowired
	private PushClientProvider pushClientProvider;

	@Test
	@Disabled
	@DisplayName("Send push notification to application with CF Binding")
	void pushToApplicationCFBinding() {
		PushResponse response = pushClientProvider.getCfBindingClient().pushToApplication(PushPayload.builder()
				.alert("Simple push message")
				.build());

		assertThat(response.getStatus().getCode(), is(0));
	}

	@Test
	@DisplayName("Send push notification to application with API-Key settings file")
	void pushToApplicationApiKeySettings() {
		PushResponse response = pushClientProvider.getApiKeyClient().pushToApplication(PushPayload.builder()
				.alert("Simple push message")
				.build());

		assertThat(response.getStatus().getCode(), is(0));
	}

	@Test
	@DisplayName("Send push notification to application with x509 settings file")
	void pushToApplicationX509Settings() {
		PushResponse response = pushClientProvider.getX509Client().pushToApplication(PushPayload.builder()
				.alert("Simple push message")
				.build());

		assertThat(response.getStatus().getCode(), is(0));
	}
}

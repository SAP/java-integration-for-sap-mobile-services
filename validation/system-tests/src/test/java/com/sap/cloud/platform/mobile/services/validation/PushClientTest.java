package com.sap.cloud.platform.mobile.services.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sap.cloud.platform.mobile.services.validation.config.MobileServicesClientConfiguration;
import com.sap.mobile.services.client.push.PushClient;
import com.sap.mobile.services.client.push.PushPayload;
import com.sap.mobile.services.client.push.PushResponse;

@SpringBootTest(classes = {
		MobileServicesClientConfiguration.class
})
public class PushClientTest {

	@Autowired
	private PushClient pushClient;

	@Test
	@DisplayName("Send push notification to application")
	void pushToApplication() {
		PushResponse response = pushClient.pushToApplication(PushPayload.builder()
				.alert("Simple push message")
				.build());

		assertThat(response.getStatus().getCode(), is(0));
	}
}

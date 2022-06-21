package com.sap.mobile.services.client.push;

import com.sap.mobile.services.client.MobileServicesSettings;

public class PushClientTest {
	public void testCompilation() throws Exception {
		PushClient pushClient = new PushClientBuilder()
				.build(MobileServicesSettings.fromResource("mobileservices-config.json"));

		ApnsNotification apns = ApnsNotification.builder().alertBody("alert_body").build();
		PushPayload pushPayload = PushPayload.builder().alert("alert").apns(apns).build();

		pushClient.pushToApplication(pushPayload);
	}
}

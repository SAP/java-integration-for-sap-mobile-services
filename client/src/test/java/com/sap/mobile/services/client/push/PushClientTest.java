package com.sap.mobile.services.client.push;

import java.util.HashMap;
import java.util.Map;

import com.sap.mobile.services.client.MobileServicesSettings;

public class PushClientTest {
	public void testCompilation() throws Exception {
		PushClient pushClient = new PushClientBuilder()
				.build(MobileServicesSettings.fromResource("mobileservices-config.json"));

		ApnsNotification apns = ApnsNotification.builder().alertBody("alert_body").build();
		Map<String,Object> data = new HashMap<>();
		data.put("Key", "Value");
		PushPayload pushPayload = PushPayload.builder().alert("alert").apns(apns).data(data).build();

		pushClient.pushToApplication(pushPayload);
	}
}

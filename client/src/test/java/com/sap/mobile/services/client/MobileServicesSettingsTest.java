package com.sap.mobile.services.client;

import org.junit.Assert;
import org.junit.Test;

public class MobileServicesSettingsTest {

	@Test
	public void testSettings() throws Exception {
		MobileServicesSettings settings = MobileServicesSettings.fromResource("mobileservices-config.json");
		Assert.assertEquals(
				"https://service.key.url.com/2787d5ea-17cb-4c71-85ca-a9600d4b97c9",
				settings.getServices().stream().filter(s -> "push".equals(s.getName())).findFirst().get()
						.getServiceKeys().get(0)
						.getUrl());
		Assert.assertEquals("apiKey", settings.getServices()
				.stream().filter(s -> "push".equals(s.getName())).findFirst().get().getServiceKeys().get(0)
				.getApiKey());
	}
}

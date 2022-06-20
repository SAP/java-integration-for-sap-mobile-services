package com.sap.mobile.services.client;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;

import com.sap.mobile.services.client.ApiKeyAuthorizationRequestInterceptor;
import com.sap.mobile.services.client.BuildProperties;
import com.sap.mobile.services.client.MobileServicesSettings;
import com.sap.mobile.services.client.ServiceKeyClientConfiguration;

public class ServiceKeyClientConfigurationTest {

	private ServiceKeyClientConfiguration testee;

	@Before
	public void prepare() {
		testee = ServiceKeyClientConfiguration.builder()
				.buildProperties(BuildProperties.getInstance())
				.applicationId("com.sap.mobile.sevices.application")
				.serviceKey(MobileServicesSettings.ServiceKey.builder()
						.apiKey("api-key")
						.alias("mobileservices")
						.url("https://mobile-services.tld/e0fc2a72-cac1-472a-ab8e-6478a1e46c40")
						.build())
				.build();
	}

	@Test
	public void testGetRootUri() {
		assertThat(testee.getRootUri(), is("https://mobile-services.tld/e0fc2a72-cac1-472a-ab8e-6478a1e46c40"));
	}

	@Test
	public void testGetAuthInterceptor() {
		assertThat(testee.getAuthInterceptor(), is(instanceOf(ApiKeyAuthorizationRequestInterceptor.class)));
	}

}

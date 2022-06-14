package com.sap.mobile.services.client.push;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.sap.cloud.security.xsuaa.tokenflows.ClientCredentialsTokenFlow;

public class XsuaaClientConfigurationTest {

	private XsuaaClientConfiguration testee;

	@Before
	public void prepare() {
		testee = XsuaaClientConfiguration.builder()
				.buildProperties(BuildProperties.getInstance())
				.applicationId("com.sap.mobile.sevices.application")
				.xsuaaTokenFlow(Mockito.mock(ClientCredentialsTokenFlow.class))
				.rootUri("https://mobile-services.tld/e0fc2a72-cac1-472a-ab8e-6478a1e46c40")
				.build();
	}

	@Test
	public void testGetRootUri() {
		assertThat(testee.getRootUri(), is("https://mobile-services.tld/e0fc2a72-cac1-472a-ab8e-6478a1e46c40"));
	}

	@Test
	public void testGetAuthInterceptor() {
		assertThat(testee.getAuthInterceptor(), is(instanceOf(XsuaaAuthorizationRequestInterceptor.class)));
	}

}

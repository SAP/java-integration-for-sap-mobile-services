package com.sap.mobile.services.client;

import com.sap.cloud.security.client.HttpClientFactory;
import com.sap.cloud.security.config.ClientCredentials;
import com.sap.cloud.security.config.ClientIdentity;
import com.sap.cloud.security.xsuaa.client.DefaultOAuth2TokenService;
import com.sap.cloud.security.xsuaa.client.XsuaaDefaultEndpoints;
import com.sap.cloud.security.xsuaa.tokenflows.ClientCredentialsTokenFlow;
import com.sap.cloud.security.xsuaa.tokenflows.XsuaaTokenFlows;

public class XsuaaTokenFlowFactory {

	public ClientCredentialsTokenFlow createClientCredentialsTokenFlow(final MobileServicesBinding binding) {
		final ClientIdentity clientIdentity = new ClientCredentials(binding.getClientConfiguration().getClientId(),
				binding.getClientConfiguration().getClientSecret());
		return createTokenFlows(clientIdentity, binding.getClientConfiguration().getUrl()).clientCredentialsTokenFlow();
	}

	XsuaaTokenFlows createTokenFlows(final ClientIdentity clientIdentity, final String oauthEndpointUrlBase) {
		return new XsuaaTokenFlows(
				new DefaultOAuth2TokenService(HttpClientFactory.create(clientIdentity)),
				new XsuaaDefaultEndpoints(oauthEndpointUrlBase, null),
				clientIdentity);
	}
}

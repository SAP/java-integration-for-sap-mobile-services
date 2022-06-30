package com.sap.mobile.services.client;

import org.springframework.http.client.ClientHttpRequestInterceptor;

import com.sap.cloud.security.xsuaa.tokenflows.ClientCredentialsTokenFlow;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter(AccessLevel.PACKAGE)
@SuperBuilder
public class XsuaaClientConfiguration extends ClientConfiguration {
	private String rootUri;
	private ClientCredentialsTokenFlow xsuaaTokenFlow;

	@Override
	ClientHttpRequestInterceptor getAuthInterceptor() {
		return new XsuaaAuthorizationRequestInterceptor(xsuaaTokenFlow, this.getTenantSupplier());
	}

}

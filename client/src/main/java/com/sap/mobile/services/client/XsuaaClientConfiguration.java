package com.sap.mobile.services.client;

import com.sap.cloud.security.xsuaa.tokenflows.ClientCredentialsTokenFlow;

import feign.RequestInterceptor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter(AccessLevel.PACKAGE)
@SuperBuilder
public class XsuaaClientConfiguration extends ClientConfiguration {
	private String rootUri;
	private ClientCredentialsTokenFlow xsuaaTokenFlow;

	@Override
	RequestInterceptor getAuthInterceptor() {
		return new XsuaaAuthorizationRequestInterceptor(xsuaaTokenFlow);
	}

}

package com.sap.mobile.services.client;

import feign.RequestInterceptor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter(AccessLevel.PACKAGE)
@SuperBuilder
public class ServiceKeyClientConfiguration extends ClientConfiguration {
	private MobileServicesSettings.ServiceKey serviceKey;

	@Override
	String getRootUri() {
		return serviceKey.getUrl();
	}

	@Override
	RequestInterceptor getAuthInterceptor() {
		return new ApiKeyAuthorizationRequestInterceptor(this.serviceKey);
	}

}

package com.sap.mobile.services.client;

import org.springframework.http.client.ClientHttpRequestInterceptor;

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
	ClientHttpRequestInterceptor getAuthInterceptor() {
		return new ApiKeyAuthorizationRequestInterceptor(this.serviceKey, this.getTenantSupplier());
	}

}

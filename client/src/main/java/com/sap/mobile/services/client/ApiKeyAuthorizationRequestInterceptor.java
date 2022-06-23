package com.sap.mobile.services.client;

import com.sap.mobile.services.client.MobileServicesSettings.ServiceKey;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class ApiKeyAuthorizationRequestInterceptor implements RequestInterceptor {

	private final ServiceKey serviceKey;

	@Override
	public void apply(RequestTemplate requestTemplate) {
		requestTemplate.header(Constants.Headers.SERVICE_KEY_AUTH_HEADER,  this.serviceKey.getApiKey());
	}
}

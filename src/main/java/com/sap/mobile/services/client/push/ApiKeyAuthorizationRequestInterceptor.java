package com.sap.mobile.services.client.push;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import com.sap.mobile.services.client.push.MobileServicesSettings.ServiceKey;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class ApiKeyAuthorizationRequestInterceptor implements ClientHttpRequestInterceptor {

	private final ServiceKey serviceKey;

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		request.getHeaders().set(Constants.Headers.SERVICE_KEY_AUTH_HEADER, this.serviceKey.getApiKey());
		return execution.execute(request, body);
	}
}

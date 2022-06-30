package com.sap.mobile.services.client;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import com.sap.mobile.services.client.MobileServicesSettings.ServiceKey;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class ApiKeyAuthorizationRequestInterceptor implements ClientHttpRequestInterceptor {

	private final ServiceKey serviceKey;
	private final TenantSupplier tenantSupplier;

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		request.getHeaders().set(Constants.Headers.SERVICE_KEY_AUTH_HEADER, this.serviceKey.getApiKey());
		this.tenantSupplier.get()
				.ifPresent(tenantId -> request.getHeaders().set(Constants.Headers.TENANT_ID_HEADER, tenantId));
		return execution.execute(request, body);
	}
}

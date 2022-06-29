package com.sap.mobile.services.client;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class TenantHeaderRequestInterceptor implements ClientHttpRequestInterceptor {

	private final TenantSupplier tenantSupplier;

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		this.tenantSupplier.get().ifPresent(tenantId -> {
			request.getHeaders().set(Constants.Headers.TENANT_ID_HEADER, tenantId);
		});
		return execution.execute(request, body);
	}
}

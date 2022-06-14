package com.sap.mobile.services.client.push;

import org.springframework.http.client.ClientHttpRequestInterceptor;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter(AccessLevel.PACKAGE)
@SuperBuilder
class CustomAuthClientConfiguration extends ClientConfiguration {
	private final String rootUri;
	private final CustomAuthHeaderSupplier authHeaderSupplier;

	@Override
	ClientHttpRequestInterceptor getAuthInterceptor() {
		return (request, body, execution) -> {
			HttpHeader header = authHeaderSupplier.get();
			request.getHeaders().set(header.getName(), header.getValue());
			return execution.execute(request, body);
		};
	}
}

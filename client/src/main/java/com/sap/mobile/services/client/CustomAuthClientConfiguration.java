package com.sap.mobile.services.client;

import feign.RequestInterceptor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter(AccessLevel.PACKAGE)
@SuperBuilder
public class CustomAuthClientConfiguration extends ClientConfiguration {
	private final String rootUri;
	private final CustomAuthHeaderSupplier authHeaderSupplier;

	@Override
	RequestInterceptor getAuthInterceptor() {
		return (requestTemplate) -> {
			HttpHeader header = authHeaderSupplier.get();
			requestTemplate.header(header.getName(), header.getValue());
		};
	}
}

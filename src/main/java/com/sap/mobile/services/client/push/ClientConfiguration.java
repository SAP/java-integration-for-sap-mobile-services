package com.sap.mobile.services.client.push;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.http.client.ClientHttpRequestInterceptor;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter(AccessLevel.PACKAGE)
@SuperBuilder
abstract class ClientConfiguration {
	private BuildProperties buildProperties;

	private String applicationId;

	private Duration connectTimeout;
	private Duration readTimeout;
	private Supplier<Optional<String>> tenantResolver;

	abstract String getRootUri();

	abstract ClientHttpRequestInterceptor getAuthInterceptor();
}

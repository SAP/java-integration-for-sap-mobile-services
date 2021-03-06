package com.sap.mobile.services.client;

import java.time.Duration;

import org.springframework.http.client.ClientHttpRequestInterceptor;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
abstract public class ClientConfiguration {
	private BuildProperties buildProperties;

	private String applicationId;

	private Duration connectTimeout;
	private Duration readTimeout;
	private TenantSupplier tenantSupplier;

	abstract String getRootUri();

	abstract ClientHttpRequestInterceptor getAuthInterceptor();
}

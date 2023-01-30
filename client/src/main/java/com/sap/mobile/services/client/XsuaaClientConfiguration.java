package com.sap.mobile.services.client;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.client.ClientHttpRequestInterceptor;

import com.sap.cloud.security.xsuaa.tokenflows.ClientCredentialsTokenFlow;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter(AccessLevel.PACKAGE)
@SuperBuilder
public class XsuaaClientConfiguration extends ClientConfiguration {
	private String rootUri;
	private ClientCredentialsTokenFlow xsuaaTokenFlow;

	// if absent, default to DEDICATED to prevent a breaking change
	// see https://github.com/SAP/java-integration-for-sap-mobile-services/issues/191
	@Builder.Default
	private TenantMode tenantMode = TenantMode.DEDICATED;

	@Override
	ClientHttpRequestInterceptor getAuthInterceptor() {
		return new XsuaaAuthorizationRequestInterceptor(xsuaaTokenFlow, this.getTenantSupplier(), tenantMode);
	}

	@Getter
	public enum TenantMode {
		SHARED, DEDICATED, EXTERNAL;

		public static Optional<TenantMode> parse(final String mode) {
			return Arrays.stream(TenantMode.values())
					.filter(m -> m.name().equalsIgnoreCase(mode))
					.findFirst();
		}

		public static String acceptedTypes() {
			return Arrays.stream(TenantMode.values()).map(TenantMode::name)
					.collect(Collectors.joining(", "));
		}
	}
}

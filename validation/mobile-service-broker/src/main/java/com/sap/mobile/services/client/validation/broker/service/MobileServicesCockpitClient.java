package com.sap.mobile.services.client.validation.broker.service;

import java.net.URI;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.cloudfoundry.client.v3.serviceinstances.ServiceInstanceResource;
import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.mobile.services.client.validation.broker.model.ServiceKeyRequest;

import lombok.Builder;
import lombok.Data;

@Service
public class MobileServicesCockpitClient {

	private final RestTemplate restTemplate;

	public MobileServicesCockpitClient(final PasswordGrantTokenProvider tokenProvider,
			final ConnectionContext context, RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.additionalInterceptors((request, body, execution) -> {
			final String token = tokenProvider.getToken(context).block();
			request.getHeaders().set(HttpHeaders.AUTHORIZATION, token);
			return execution.execute(request, body);
		}).build();
	}

	public void restoreApp(final ServiceInstanceResource instance) {
		final URI cockpitUri = getCockpitApiUrl(instance);
		final URI restoreUri = UriComponentsBuilder.fromUri(cockpitUri)
				.path("/app/{appId}/restore")
				.build(instance.getName());
		restTemplate.postForEntity(restoreUri, null, String.class);
	}

	public void deleteApp(final ServiceInstanceResource instance) {
		final URI cockpitUri = getCockpitApiUrl(instance);
		final URI deletionUri = UriComponentsBuilder.fromUri(cockpitUri)
				.path("/app/{appId}")
				.build(instance.getName());
		restTemplate.delete(deletionUri);
	}

	public Map<?, ?> createServiceKey(final ServiceInstanceResource instance, final ServiceKeyRequest serviceKeyRequest) {
		final URI cockpitUri = getCockpitApiUrl(instance);
		final URI serviceKeyUri = UriComponentsBuilder.fromUri(cockpitUri)
				.path("/app/{appId}/service/{service}/key")
				.build(instance.getName(), serviceKeyRequest.getServiceName());

		final CockpitServiceKeyCreationRequest requestBody = CockpitServiceKeyCreationRequest.builder()
				.alias(RandomStringUtils.randomAlphabetic(27))
				.credentialType(serviceKeyRequest.isX509() ? "x509" : null)
				.scopes(serviceKeyRequest.getScopes())
				.x509Config(serviceKeyRequest.isX509() ? CockpitX509Config.DEFAULT : null)
				.build();

		return restTemplate.postForEntity(serviceKeyUri, requestBody, Map.class).getBody();
	}

	public URI getCockpitApiUrl(final ServiceInstanceResource instance) {
		final UriComponents dashboardUrl = UriComponentsBuilder.fromUriString(instance.getDashboardUrl()).build();
		return UriComponentsBuilder.fromUriString(instance.getDashboardUrl())
				.host(dashboardUrl.getHost().replace("-web", "-api"))
				.replacePath(dashboardUrl.getPath().replace("/index.html", ""))
				.fragment(null)
				.build().toUri();
	}

	@Data
	@Builder
	private static class CockpitServiceKeyCreationRequest {
		@JsonProperty("alias")
		private String alias;

		@JsonProperty("credential-type")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		private String credentialType;

		@JsonProperty("scopes")
		@Builder.Default
		private Set<String> scopes = new HashSet<>();

		@JsonProperty("x509")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		private CockpitX509Config x509Config;
	}

	@Data
	@Builder
	private static class CockpitX509Config {

		static final CockpitX509Config DEFAULT = CockpitX509Config.builder()
				.keyLength(2048).validity(1).validityType("DAYS").build();

		@JsonProperty("key-length")
		private int keyLength;

		@JsonProperty("validity")
		private int validity;

		@JsonProperty("validity-type")
		private String validityType;
	}
}

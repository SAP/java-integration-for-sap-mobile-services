package com.sap.mobile.services.client.validation.broker.service;

import java.net.URI;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.sap.cloudfoundry.client.facade.CloudControllerClient;
import com.sap.cloudfoundry.client.facade.domain.CloudServiceInstance;
import com.sap.cloudfoundry.client.facade.domain.CloudServiceOffering;
import com.sap.cloudfoundry.client.facade.domain.CloudSpace;
import com.sap.cloudfoundry.client.facade.oauth2.OAuth2AccessTokenWithAdditionalInfo;

@Service
public class MobileServicesCockpitClient {

	private final CloudControllerClient cfClient;
	private final CloudSpace target;
	private final RestTemplate restTemplate;

	public MobileServicesCockpitClient(final CloudControllerClient cfClient, final CloudSpace target,
			final RestTemplateBuilder restTemplateBuilder) {
		this.cfClient = cfClient;
		this.target = target;
		this.restTemplate = restTemplateBuilder.additionalInterceptors((request, body, execution) -> {
			final OAuth2AccessTokenWithAdditionalInfo tokenResponse = cfClient.login();
			final String token = tokenResponse.getOAuth2AccessToken().getTokenValue();
			request.getHeaders().set(HttpHeaders.AUTHORIZATION, String.format("bearer %s", token));
			return execution.execute(request, body);
		}).build();
	}

	public void restoreApp(final CloudServiceInstance instance) {
		final URI cockpitUri = getCockpitApiUrl(instance);
		final URI restoreUri = UriComponentsBuilder.fromUri(cockpitUri)
				.path("/app/{appId}/restore")
				.build(instance.getName());
		restTemplate.postForEntity(restoreUri, null, String.class);
	}

	public void deleteApp(final CloudServiceInstance instance) {
		final URI cockpitUri = getCockpitApiUrl(instance);
		final URI deletionUri = UriComponentsBuilder.fromUri(cockpitUri)
				.path("/app/{appId}")
				.build(instance.getName());
		restTemplate.delete(deletionUri);
	}


	public URI getCockpitApiUrl(final CloudServiceInstance instance) {
		final CloudServiceOffering serviceOffering = cfClient.getServiceOfferings().stream().filter(service -> {
			return service.getName().equals(instance.getLabel());
		}).findFirst().orElseThrow(() -> new IllegalStateException("Failed to retrieve cockpit-api URL"));

		final String supportUrl = String.valueOf(serviceOffering.getExtra().get("supportUrl"));
		final String cockpitApiUrl = supportUrl.replace("-web", "-api");
		return UriComponentsBuilder.fromUriString(cockpitApiUrl)
				.path("/cockpit/v1/org/{org}/space/{space}")
				.build(target.getOrganization().getGuid(), target.getGuid());
	}

}

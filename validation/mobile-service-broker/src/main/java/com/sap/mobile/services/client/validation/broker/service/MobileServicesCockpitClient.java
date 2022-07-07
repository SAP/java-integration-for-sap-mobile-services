package com.sap.mobile.services.client.validation.broker.service;

import java.net.URI;

import org.cloudfoundry.client.v3.serviceinstances.ServiceInstanceResource;
import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

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

	public URI getCockpitApiUrl(final ServiceInstanceResource instance) {
		final UriComponents dashboardUrl = UriComponentsBuilder.fromUriString(instance.getDashboardUrl()).build();
		return UriComponentsBuilder.fromUriString(instance.getDashboardUrl())
				.host(dashboardUrl.getHost().replace("-web", "-api"))
				.replacePath(dashboardUrl.getPath().replace("/index.html", ""))
				.fragment(null)
				.build().toUri();
	}

}

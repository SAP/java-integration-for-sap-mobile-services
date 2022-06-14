package com.sap.mobile.services.client.push.mock;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasEntry;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.test.web.client.RequestMatcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter(AccessLevel.PACKAGE)
public final class RequestExpectation<Response extends ResponseDefinition<Response>> {

	private final ObjectMapper mapper = new ObjectMapper();

	private final MockPushServer server;
	private final Response responseDefinition;
	private final List<RequestMatcher> additionalMatchers = new ArrayList<>();

	public MockPushServer and() {
		return server;
	}

	public Response andRespond() {
		return responseDefinition;
	}

	public RequestExpectation<Response> with(final RequestMatcher requestMatcher) {
		additionalMatchers.add(requestMatcher);
		return this;
	}

	public RequestExpectation<Response> withServiceKeyAuth(final String serviceKey) {
		return with(req -> {
			final HttpHeaders headers = req.getHeaders();
			assertThat(headers, hasEntry(is(equalToIgnoringCase("X-API-Key")), contains(serviceKey)));
		});
	}

	public RequestExpectation<Response> withBearerToken(final String token) {
		return with(req -> {
			final HttpHeaders headers = req.getHeaders();
			assertThat(headers, hasEntry(is(equalToIgnoringCase(HttpHeaders.AUTHORIZATION)), contains("bearer " + token)));
		});
	}

	public RequestExpectation<Response> forTenant(final String tenantId) {
		if (server.isApiKeyAuth()) {
			return with(req -> {
				final HttpHeaders headers = req.getHeaders();
				assertThat(headers, hasEntry(is(equalToIgnoringCase("x-tenant-id")), contains(tenantId)));
			});
		}

		return this;
	}

	public RequestExpectation<Response> withJsonBody(final String jsonString) {
		additionalMatchers.add(req -> {
			final String body = ((MockClientHttpRequest) req).getBodyAsString();
			final JsonNode expected = mapper.readTree(jsonString);
			final JsonNode actual = mapper.readTree(body);
			assertThat(actual, is(expected));
		});
		return this;
	}

	public RequestExpectation<Response> withJsonBodyFromResource(final String resourceName) throws IOException {
		try (final InputStream stream = getClass().getClassLoader().getResourceAsStream(resourceName)) {
			return withJsonBody(IOUtils.toString(stream, StandardCharsets.UTF_8));
		}
	}
}

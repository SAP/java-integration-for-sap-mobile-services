package com.sap.mobile.services.client;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.web.client.RestClientException;

import com.sap.cloud.security.xsuaa.client.OAuth2TokenResponse;
import com.sap.cloud.security.xsuaa.tokenflows.ClientCredentialsTokenFlow;
import com.sap.cloud.security.xsuaa.tokenflows.TokenFlowException;
import com.sap.mobile.services.client.XsuaaAuthorizationRequestInterceptor;

public class XsuaaAuthorizationRequestInterceptorTest {

	private MockClientHttpRequest initialRequest;
	private MockClientHttpResponse expectedResponse;
	private OAuth2TokenResponse oAuth2TokenResponse;
	private TenantSupplier tenantSupplier;

	private ClientCredentialsTokenFlow tokenFlow;
	private XsuaaAuthorizationRequestInterceptor testee;

	@Before
	public void prepare() {
		tokenFlow = Mockito.mock(ClientCredentialsTokenFlow.class);
		tenantSupplier = Mockito.mock(TenantSupplier.class);

		initialRequest = new MockClientHttpRequest(HttpMethod.GET, URI.create("https://service.tld/path"));
		initialRequest.getHeaders().add("x-dummy-header", "x-dummy-value");
		expectedResponse = new MockClientHttpResponse("Hello World".getBytes(StandardCharsets.UTF_8), HttpStatus.OK);
		oAuth2TokenResponse = new OAuth2TokenResponse("my-secret-jwt", 60, "refresh-me");

		testee = new XsuaaAuthorizationRequestInterceptor(tokenFlow, tenantSupplier, XsuaaClientConfiguration.TenantMode.SHARED);
	}

	@Test
	public void testInterceptTenantModeShared() throws Exception {
		testee = new XsuaaAuthorizationRequestInterceptor(tokenFlow, tenantSupplier, XsuaaClientConfiguration.TenantMode.SHARED);

		final ClientHttpRequestExecution execution = Mockito.mock(ClientHttpRequestExecution.class);
		Mockito.when(execution.execute(any(), any())).thenAnswer(invocation -> {
			final HttpRequest request = invocation.getArgument(0);
			final byte[] body = invocation.getArgument(1);

			assertThat(body, is(new byte[0]));
			assertThat(request.getMethod(), is(initialRequest.getMethod()));
			assertThat(request.getURI(), is(initialRequest.getURI()));
			assertThat(request.getHeaders().getFirst("x-dummy-header"), is("x-dummy-value"));
			assertThat(request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION), is("bearer my-secret-jwt"));

			return expectedResponse;
		});

		Mockito.when(tenantSupplier.get()).thenReturn(Optional.empty());
		Mockito.when(tokenFlow.execute()).thenReturn(oAuth2TokenResponse);

		final ClientHttpResponse response = testee.intercept(initialRequest, new byte[0], execution);
		assertThat(response, is(expectedResponse));
		Mockito.verify(execution).execute(any(), any());
	}

	@Test
	public void testInterceptForTenantTenantModeShared() throws Exception {
		testee = new XsuaaAuthorizationRequestInterceptor(tokenFlow, tenantSupplier, XsuaaClientConfiguration.TenantMode.SHARED);

		final String tenantZoneId = UUID.randomUUID().toString();
		final ClientHttpRequestExecution execution = Mockito.mock(ClientHttpRequestExecution.class);
		Mockito.when(execution.execute(any(), any())).thenAnswer(invocation -> {
			final HttpRequest request = invocation.getArgument(0);
			final byte[] body = invocation.getArgument(1);

			assertThat(body, is(new byte[0]));
			assertThat(request.getMethod(), is(initialRequest.getMethod()));
			assertThat(request.getURI(), is(initialRequest.getURI()));
			assertThat(request.getHeaders().getFirst("x-dummy-header"), is("x-dummy-value"));
			assertThat(request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION), is("bearer my-secret-jwt"));

			return expectedResponse;
		});

		Mockito.when(tenantSupplier.get()).thenReturn(Optional.of(tenantZoneId));
		Mockito.when(tokenFlow.zoneId(tenantZoneId)).thenReturn(tokenFlow);
		Mockito.when(tokenFlow.execute()).thenReturn(oAuth2TokenResponse);

		final ClientHttpResponse response = testee.intercept(initialRequest, new byte[0], execution);
		assertThat(response, is(expectedResponse));
		Mockito.verify(tokenFlow).zoneId(tenantZoneId);
		Mockito.verify(execution).execute(any(), any());
	}

	@Test
	public void testInterceptTenantModeDedicated() throws Exception {
		testee = new XsuaaAuthorizationRequestInterceptor(tokenFlow, tenantSupplier, XsuaaClientConfiguration.TenantMode.DEDICATED);

		final ClientHttpRequestExecution execution = Mockito.mock(ClientHttpRequestExecution.class);
		Mockito.when(execution.execute(any(), any())).thenAnswer(invocation -> {
			final HttpRequest request = invocation.getArgument(0);
			final byte[] body = invocation.getArgument(1);

			assertThat(body, is(new byte[0]));
			assertThat(request.getMethod(), is(initialRequest.getMethod()));
			assertThat(request.getURI(), is(initialRequest.getURI()));
			assertThat(request.getHeaders().getFirst("x-dummy-header"), is("x-dummy-value"));
			assertThat(request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION), is("bearer my-secret-jwt"));

			return expectedResponse;
		});

		Mockito.when(tenantSupplier.get()).thenReturn(Optional.empty());
		Mockito.when(tokenFlow.execute()).thenReturn(oAuth2TokenResponse);

		final ClientHttpResponse response = testee.intercept(initialRequest, new byte[0], execution);
		assertThat(response, is(expectedResponse));
		Mockito.verify(execution).execute(any(), any());
	}

	@Test
	public void testInterceptForTenantTenantModeDedicated() throws Exception {
		testee = new XsuaaAuthorizationRequestInterceptor(tokenFlow, tenantSupplier, XsuaaClientConfiguration.TenantMode.DEDICATED);

		final String tenantZoneId = UUID.randomUUID().toString();
		final ClientHttpRequestExecution execution = Mockito.mock(ClientHttpRequestExecution.class);
		Mockito.when(execution.execute(any(), any())).thenAnswer(invocation -> {
			final HttpRequest request = invocation.getArgument(0);
			final byte[] body = invocation.getArgument(1);

			assertThat(body, is(new byte[0]));
			assertThat(request.getMethod(), is(initialRequest.getMethod()));
			assertThat(request.getURI(), is(initialRequest.getURI()));
			assertThat(request.getHeaders().getFirst("x-dummy-header"), is("x-dummy-value"));
			assertThat(request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION), is("bearer my-secret-jwt"));
			assertThat(request.getHeaders().getFirst("x-tenant-id"), is(tenantZoneId));

			return expectedResponse;
		});

		Mockito.when(tenantSupplier.get()).thenReturn(Optional.of(tenantZoneId));
		Mockito.when(tokenFlow.execute()).thenReturn(oAuth2TokenResponse);

		final ClientHttpResponse response = testee.intercept(initialRequest, new byte[0], execution);
		assertThat(response, is(expectedResponse));
		Mockito.verify(tokenFlow, Mockito.never()).zoneId(tenantZoneId);
		Mockito.verify(execution).execute(any(), any());
	}

	@Test
	public void testInterceptWithXsuaaExceptions() throws Exception {
		final ClientHttpRequestExecution execution = (req, body) -> {
			fail("must not be called");
			return null;
		};

		Mockito.when(tenantSupplier.get()).thenReturn(Optional.empty());
		Mockito.when(tokenFlow.execute())
				.thenThrow(new TokenFlowException("Error retrieving JWT token. " +
						"Received status code 401 UNAUTHORIZED. Call to XSUAA was not successful"))
				.thenThrow(new IllegalArgumentException("Client credentials flow request is not valid. " +
						"Make sure all mandatory fields are set."));

		assertThrows(RestClientException.class, () -> {
			testee.intercept(initialRequest, new byte[0], execution);
		});

		assertThrows(RestClientException.class, () -> {
			testee.intercept(initialRequest, new byte[0], execution);
		});
	}
}

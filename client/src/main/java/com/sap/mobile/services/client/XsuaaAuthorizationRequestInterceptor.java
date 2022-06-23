package com.sap.mobile.services.client;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClientException;

import com.sap.cloud.security.xsuaa.client.OAuth2ServiceException;
import com.sap.cloud.security.xsuaa.client.OAuth2TokenResponse;
import com.sap.cloud.security.xsuaa.tokenflows.ClientCredentialsTokenFlow;
import com.sap.cloud.security.xsuaa.tokenflows.TokenFlowException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Slf4j
class XsuaaAuthorizationRequestInterceptor implements ClientHttpRequestInterceptor {

	private final ClientCredentialsTokenFlow tokenFlow;

	@Override
	public ClientHttpResponse intercept(final HttpRequest request, final byte[] body,
			final ClientHttpRequestExecution execution)
			throws IOException {
		final OAuth2TokenResponse tokenResponse;
		final String tenantId = request.getHeaders().getFirst(Constants.Headers.TENANT_ID_HEADER);
		try {
			if (tenantId != null) {
				request.getHeaders().remove(Constants.Headers.TENANT_ID_HEADER);
				tokenResponse = tokenFlow.zoneId(tenantId).execute();
			} else {
				tokenResponse = tokenFlow.execute();
			}
		} catch (IllegalArgumentException | TokenFlowException e) {
			log.error("Failed to fetch XSUAA token", e);

			if (tenantId != null && isMissingTenantError(e)) {
				throw new NoSuchTenantException(tenantId);
			}

			throw new RestClientException("Failed to fetch XSUAA token", e);
		}

		request.getHeaders().set(HttpHeaders.AUTHORIZATION,
				String.format("%s %s", tokenResponse.getTokenType(), tokenResponse.getAccessToken()));
		return execution.execute(request, body);
	}

	private boolean isMissingTenantError(final Exception e) {
		final Throwable cause = e.getCause();
		if (cause instanceof OAuth2ServiceException) {
			final Integer statusCode = ((OAuth2ServiceException) cause).getHttpStatusCode();
			return statusCode != null && statusCode.equals(HttpStatus.NOT_FOUND.value());
		}

		return false;
	}
}

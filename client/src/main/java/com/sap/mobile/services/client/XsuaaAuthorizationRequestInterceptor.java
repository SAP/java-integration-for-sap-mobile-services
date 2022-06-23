package com.sap.mobile.services.client;

import java.util.Collection;

import com.sap.cloud.security.xsuaa.client.OAuth2ServiceException;
import com.sap.cloud.security.xsuaa.client.OAuth2TokenResponse;
import com.sap.cloud.security.xsuaa.tokenflows.ClientCredentialsTokenFlow;
import com.sap.cloud.security.xsuaa.tokenflows.TokenFlowException;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Slf4j
class XsuaaAuthorizationRequestInterceptor implements RequestInterceptor {

	private final ClientCredentialsTokenFlow tokenFlow;

	@Override
	public void apply(RequestTemplate requestTemplate) {
		final OAuth2TokenResponse tokenResponse;
		final String tenantId = this.getFirstHeader(requestTemplate, Constants.Headers.TENANT_ID_HEADER);
		try {
			if (tenantId != null) {
				requestTemplate.headers().remove(Constants.Headers.TENANT_ID_HEADER);
				tokenResponse = tokenFlow.zoneId(tenantId).execute();
			} else {
				tokenResponse = tokenFlow.execute();
			}
		} catch (IllegalArgumentException | TokenFlowException e) {
			log.error("Failed to fetch XSUAA token", e);

			if (tenantId != null && isMissingTenantError(e)) {
				throw new NoSuchTenantException(tenantId);
			}

			throw new ClientException("Failed to fetch XSUAA token", e);
		}

		requestTemplate.header("Authorization",
				String.format("%s %s", tokenResponse.getTokenType(), tokenResponse.getAccessToken()));
	}

	private String getFirstHeader(RequestTemplate requestTemplate, String header) {
		Collection<String> headers = requestTemplate.headers().get(header);
		if (headers == null || headers.isEmpty()) {
			return null;
		}
		return headers.stream().findFirst().get();
	}

	private boolean isMissingTenantError(final Exception e) {
		final Throwable cause = e.getCause();
		if (cause instanceof OAuth2ServiceException) {
			final Integer statusCode = ((OAuth2ServiceException) cause).getHttpStatusCode();
			return statusCode != null && 404 == statusCode;
		}

		return false;
	}
}

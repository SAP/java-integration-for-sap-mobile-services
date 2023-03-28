package com.sap.mobile.services.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;


public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return response.getStatusCode().isError();
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		String responseBodyText = IOUtils.toString(response.getBody(), StandardCharsets.UTF_8);
		this.handleServiceSpecificErrors(response, responseBodyText);
		final HttpStatus status = response.getStatusCode();
		switch (status) {
			case UNAUTHORIZED:
				throw new ClientUnauthorizedException(responseBodyText, response.getHeaders());
			case TOO_MANY_REQUESTS:
				throw new TrialLimitExceededException(responseBodyText, response.getHeaders());
			default:
		}
		// Generic error handlers for undefined 4** and 5**
		if (status.is4xxClientError()) {
			throw new ClientErrorException(status.name(), responseBodyText, response.getHeaders());
		}
		if (status.is5xxServerError()) {
			throw new ServerErrorException(status.name(), responseBodyText, response.getHeaders());
		}
	}

	protected void handleServiceSpecificErrors(ClientHttpResponse response, String responseBodyText) throws IOException {
	}
}

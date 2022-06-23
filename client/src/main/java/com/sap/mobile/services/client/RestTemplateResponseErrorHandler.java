package com.sap.mobile.services.client;

import java.io.IOException;

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
		final HttpStatus status = response.getStatusCode();
		this.handleServiceSpecificErrors(response);
		switch (status) {
			case UNAUTHORIZED:
				throw new ClientUnauthorizedException();
			case TOO_MANY_REQUESTS:
				throw new TrialLimitExceededException();
			default:
		}
		// Generic error handlers for undefined 4** and 5**
		if (status.is4xxClientError()) {
			throw new ClientErrorException(status.name());
		}
		if (status.is5xxServerError()) {
			throw new ServerErrorException(status.name());
		}
	}

	protected void handleServiceSpecificErrors(ClientHttpResponse response) throws IOException {
	}
}

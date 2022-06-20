package com.sap.mobile.services.client;

import java.io.IOException;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return response.getStatusCode().isError();
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		switch (response.getStatusCode()) {
			case UNAUTHORIZED:
				throw new ClientUnauthorizedException();
			case TOO_MANY_REQUESTS:
				throw new TrialLimitExceededException();
			// TODO more error handling
			default:
		}
	}
}

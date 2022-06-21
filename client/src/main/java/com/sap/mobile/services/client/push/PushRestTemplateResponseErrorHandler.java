package com.sap.mobile.services.client.push;

import java.io.IOException;

import org.springframework.http.client.ClientHttpResponse;

import com.sap.mobile.services.client.RestTemplateResponseErrorHandler;

class PushRestTemplateResponseErrorHandler extends RestTemplateResponseErrorHandler {

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		super.handleError(response);

		switch (response.getStatusCode()) {
			case UNPROCESSABLE_ENTITY:
				throw new NoMessageSentException();
			// TODO more error handling
			default:
		}
	}
}

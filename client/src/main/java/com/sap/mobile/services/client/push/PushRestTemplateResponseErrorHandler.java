package com.sap.mobile.services.client.push;

import java.io.IOException;

import org.apache.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.mobile.services.client.RestTemplateResponseErrorHandler;

class PushRestTemplateResponseErrorHandler extends RestTemplateResponseErrorHandler {
	private ObjectMapper mapper = new ObjectMapper();

	@Override
	protected void handleServiceSpecificErrors(ClientHttpResponse response, String responseBodyText) throws IOException {
		switch (response.getStatusCode().value()) {
			case HttpStatus.SC_NOT_FOUND:
				try {
					PushResponse pushResponse = mapper.readValue(responseBodyText, DTOPushResponse.class);
					throw new NoMessageSentException(pushResponse.getStatus().getMessage(), responseBodyText,
							map(response.getHeaders()), pushResponse);
				} catch (JsonProcessingException e) {
					// NOOP
				}
				throw new NoMessageSentException(responseBodyText, map(response.getHeaders()));
			case HttpStatus.SC_UNPROCESSABLE_ENTITY:
				try {
					PushResponse pushResponse = mapper.readValue(responseBodyText, DTOPushResponse.class);
					throw new MessageErrorException(responseBodyText, map(response.getHeaders()), pushResponse);
				} catch (JsonProcessingException e) {
					// NOOP
				}
				throw new MessageErrorException(responseBodyText, map(response.getHeaders()));
			default:
				try {
					PushResponse pushResponse = mapper.readValue(responseBodyText, DTOPushResponse.class);
					throw new PushClientException(response.getStatusCode().toString(), responseBodyText,
							map(response.getHeaders()), pushResponse);
				} catch (JsonProcessingException e) {
					// NOOP
				}
		}
	}
}

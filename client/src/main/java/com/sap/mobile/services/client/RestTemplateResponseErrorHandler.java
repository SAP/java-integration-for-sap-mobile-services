package com.sap.mobile.services.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
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
				throw new ClientUnauthorizedException(responseBodyText, map(response.getHeaders()));
			case TOO_MANY_REQUESTS:
				throw new TrialLimitExceededException(responseBodyText, map(response.getHeaders()));
			default:
		}
		// Generic error handlers for undefined 4** and 5**
		if (status.is4xxClientError()) {
			throw new ClientErrorException(status.name(), responseBodyText, map(response.getHeaders()));
		}
		if (status.is5xxServerError()) {
			throw new ServerErrorException(status.name(), responseBodyText, map(response.getHeaders()));
		}
	}

	protected void handleServiceSpecificErrors(ClientHttpResponse response, String responseBodyText)
			throws IOException {
	}

	protected com.sap.mobile.services.client.HttpHeaders map(HttpHeaders headers) {
		com.sap.mobile.services.client.HttpHeaders bean = new com.sap.mobile.services.client.HttpHeaders();
		Map<String, List<String>> headersMap = new HashMap<>();
		headers.forEach((key, value) -> headersMap.put(key, Collections.unmodifiableList(new ArrayList<>(value))));
		bean.setHeaders(Collections.unmodifiableMap(headers));
		return bean;
	}
}

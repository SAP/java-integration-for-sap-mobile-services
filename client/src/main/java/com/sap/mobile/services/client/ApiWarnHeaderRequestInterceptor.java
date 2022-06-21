package com.sap.mobile.services.client;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * This interceptor does a warn logging, once the X-API-Warn header is present, which is used for deprecated APIs.
 */
@Slf4j
class ApiWarnHeaderRequestInterceptor implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		final ClientHttpResponse response = execution.execute(request, body);

		String warnHeaders = response.getHeaders().getFirst(Constants.Headers.API_WARN_HEADER);
		if (warnHeaders != null) {
			log.warn(warnHeaders);
		}

		return response;
	}
}

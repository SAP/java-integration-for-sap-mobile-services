package com.sap.mobile.services.client.push;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * @see <a href=
 *      "https://github.tools.sap/northstar/integration-kernel-services/blob/master/ap-iks-api-guidance/rest-api-harmonization/1.4.md">https://github.tools.sap/northstar/integration-kernel-services/blob/master/ap-iks-api-guidance/rest-api-harmonization/1.4.md</a>
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

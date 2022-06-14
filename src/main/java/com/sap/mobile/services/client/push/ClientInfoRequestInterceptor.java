package com.sap.mobile.services.client.push;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class ClientInfoRequestInterceptor implements ClientHttpRequestInterceptor {

	private final BuildProperties props;

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		request.getHeaders().set(Constants.Headers.CLIENT_VERSION_HEADER_NAME, this.props.getVersion());
		request.getHeaders().set(Constants.Headers.CLIENT_LANGUAGE_HEADER_NAME, "Java");
		return execution.execute(request, body);
	}
}

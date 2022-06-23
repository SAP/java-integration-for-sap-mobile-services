package com.sap.mobile.services.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class ClientInfoRequestInterceptor implements RequestInterceptor {

	private final BuildProperties props;

	@Override
	public void apply(RequestTemplate requestTemplate) {
		requestTemplate.header(Constants.Headers.CLIENT_VERSION_HEADER_NAME, this.props.getVersion())
				.header(Constants.Headers.CLIENT_LANGUAGE_HEADER_NAME, "Java");
	}
}

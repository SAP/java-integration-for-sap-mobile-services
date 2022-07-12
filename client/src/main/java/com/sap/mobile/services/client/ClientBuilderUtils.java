/**
 * (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * No part of this publication may be reproduced or transmitted in any form or for any purpose
 * without the express permission of SAP SE. The information contained herein may be changed
 * without prior notice.
 */

package com.sap.mobile.services.client;

import java.util.Optional;

import org.springframework.boot.web.client.RestTemplateBuilder;

public final class ClientBuilderUtils {

	public static RestTemplateBuilder addBasicConfiguration(RestTemplateBuilder restTemplateBuilder, ClientConfiguration config) {
		restTemplateBuilder = Optional.ofNullable(config.getConnectTimeout())
				.map(restTemplateBuilder::setConnectTimeout).orElse(restTemplateBuilder);
		restTemplateBuilder = Optional.ofNullable(config.getReadTimeout()).map(restTemplateBuilder::setReadTimeout)
				.orElse(restTemplateBuilder);
		restTemplateBuilder = restTemplateBuilder.rootUri(config.getRootUri());
		return restTemplateBuilder;
	}

	public static RestTemplateBuilder addDefaultInterceptors(RestTemplateBuilder restTemplateBuilder, ClientConfiguration config) {
		restTemplateBuilder = restTemplateBuilder
				.additionalInterceptors(new ClientInfoRequestInterceptor(BuildProperties.getInstance()));
		restTemplateBuilder = restTemplateBuilder.additionalInterceptors(new ApiWarnHeaderRequestInterceptor());
		restTemplateBuilder = restTemplateBuilder.additionalInterceptors(config.getAuthInterceptor());
		return restTemplateBuilder;
	}

	public static RestTemplateBuilder defaultErrorHandler(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler());
	}

	private ClientBuilderUtils() {}
}

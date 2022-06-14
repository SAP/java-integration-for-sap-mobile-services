/**
 * (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * No part of this publication may be reproduced or transmitted in any form or for any purpose
 * without the express permission of SAP SE. The information contained herein may be changed
 * without prior notice.
 */

package com.sap.mobile.services.client.push;

import org.springframework.http.client.ClientHttpRequestInterceptor;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter(AccessLevel.PACKAGE)
@SuperBuilder
class CustomAuthClientConfiguration extends ClientConfiguration {
	private final String rootUri;
	private final CustomAuthHeaderSupplier authHeaderSupplier;

	@Override
	ClientHttpRequestInterceptor getAuthInterceptor() {
		return (request, body, execution) -> {
			HttpHeader header = authHeaderSupplier.get();
			request.getHeaders().set(header.getName(), header.getValue());
			return execution.execute(request, body);
		};
	}
}

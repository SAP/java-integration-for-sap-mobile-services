/**
 * (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * No part of this publication may be reproduced or transmitted in any form or for any purpose
 * without the express permission of SAP SE. The information contained herein may be changed
 * without prior notice.
 */

package com.sap.mobile.services.client.push;

import java.io.IOException;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return response.getStatusCode().isError();
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		switch (response.getStatusCode()) {
			case UNAUTHORIZED:
				throw new ClientUnauthorizedException();
			case UNPROCESSABLE_ENTITY:
				throw new NoMessageSentException();
			case TOO_MANY_REQUESTS:
				throw new TrialLimitExceededException();
			// TODO more error handling
			default:
		}
	}
}

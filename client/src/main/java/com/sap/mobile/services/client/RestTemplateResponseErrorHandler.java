package com.sap.mobile.services.client;

import feign.Response;
import feign.codec.ErrorDecoder;

public class RestTemplateResponseErrorHandler implements ErrorDecoder {

	@Override
	public Exception decode(String methodKey, Response response) {
		// only gets to see 4xx and 5xx responses
		Exception serviceSpecificException = this.handleServiceSpecificErrors(response);
		if (serviceSpecificException != null) {
			return serviceSpecificException;
		}

		switch (response.status()) {
			case 401:
				return new ClientUnauthorizedException();
			case 429:
				return new TrialLimitExceededException();
			default:
		}

		if (response.status() >= 400 && response.status() < 500) {
			return new ClientErrorException(methodKey);
		}
		return new ServerErrorException(methodKey);
	}

	protected Exception handleServiceSpecificErrors(Response response) {
		return null;
	}
}

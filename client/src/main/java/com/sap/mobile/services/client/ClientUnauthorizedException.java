package com.sap.mobile.services.client;

import org.springframework.http.HttpHeaders;

/**
 * Thrown when authorization failed. Please check and update the client configuration.
 */
public class ClientUnauthorizedException extends ClientException {

	ClientUnauthorizedException(String responseBodyText, HttpHeaders httpHeaders) {
		super("Authorization failed.", responseBodyText, httpHeaders);
	}
}

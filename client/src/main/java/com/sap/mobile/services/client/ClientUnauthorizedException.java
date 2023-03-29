package com.sap.mobile.services.client;

/**
 * Thrown when authorization failed. Please check and update the client configuration.
 */
public class ClientUnauthorizedException extends ClientException {

	ClientUnauthorizedException(String responseBodyText, com.sap.mobile.services.client.HttpHeaders httpHeaders) {
		super("Authorization failed.", responseBodyText, httpHeaders);
	}
}

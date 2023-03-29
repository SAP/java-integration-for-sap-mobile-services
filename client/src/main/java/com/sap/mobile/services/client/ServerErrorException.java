package com.sap.mobile.services.client;

/**
 * An 5xx Error was reported.
 */
public class ServerErrorException extends ClientException {
	ServerErrorException(String msg, String responseBodyText, com.sap.mobile.services.client.HttpHeaders httpHeaders) {
		super(msg, responseBodyText, httpHeaders);
	}
}

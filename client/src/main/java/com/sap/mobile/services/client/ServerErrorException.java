package com.sap.mobile.services.client;

import org.springframework.http.HttpHeaders;

/**
 * An 5xx Error was reported.
 */
public class ServerErrorException extends ClientException {
	ServerErrorException(String msg, String responseBodyText, HttpHeaders httpHeaders) {
		super(msg, responseBodyText, httpHeaders);
	}
}

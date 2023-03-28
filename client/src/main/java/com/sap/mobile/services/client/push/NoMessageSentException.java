package com.sap.mobile.services.client.push;

import org.springframework.http.HttpHeaders;

/**
 * No registered device was found for the request or the service URL is incorrect, see message.
 */
public class NoMessageSentException extends PushClientException {
	NoMessageSentException(String responseBodyText, HttpHeaders httpHeaders) {
		super("Push Service Instance not found.",
				responseBodyText, httpHeaders);
	}

	NoMessageSentException(String msg, String responseBodyText, HttpHeaders responseHeaders, PushResponse pushResponse) {
		super(msg, responseBodyText, responseHeaders, pushResponse);
	}
}

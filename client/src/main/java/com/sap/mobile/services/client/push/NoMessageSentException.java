package com.sap.mobile.services.client.push;

import com.sap.mobile.services.client.HttpHeaders;

/**
 * No registered device was found for the request or the service URL is incorrect, see message.
 */
public class NoMessageSentException extends PushClientException {
	NoMessageSentException(String responseBodyText, HttpHeaders httpHeaders) {
		super("Push Service Instance not found.",
				responseBodyText, httpHeaders);
	}

	NoMessageSentException(String msg, String responseBodyText, HttpHeaders httpHeaders, PushResponse pushResponse) {
		super(msg, responseBodyText, httpHeaders, pushResponse);
	}
}

package com.sap.mobile.services.client.push;

import com.sap.mobile.services.client.ClientException;

public class NoMessageSentException extends ClientException {
	NoMessageSentException() {
		super("No message was sent or queued. This is likely, because there is no push configuration.");
	}
}

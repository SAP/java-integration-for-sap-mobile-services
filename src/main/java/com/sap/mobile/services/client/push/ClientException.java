package com.sap.mobile.services.client.push;

public class ClientException extends RuntimeException {

	ClientException(String msg) {
		super(msg);
	}

	ClientException(String msg, Throwable cause) {
		super(msg, cause);
	}
}

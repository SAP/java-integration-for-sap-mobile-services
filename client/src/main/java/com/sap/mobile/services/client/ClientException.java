package com.sap.mobile.services.client;

public class ClientException extends RuntimeException {

	protected ClientException(String msg) {
		super(msg);
	}

	protected ClientException(String msg, Throwable cause) {
		super(msg, cause);
	}
}

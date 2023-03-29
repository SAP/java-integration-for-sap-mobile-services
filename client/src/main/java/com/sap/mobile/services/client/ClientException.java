package com.sap.mobile.services.client;

import lombok.Getter;

@Getter
/**
 * Generic client exception
 */
public class ClientException extends RuntimeException {

	/**
	 * The log correlation ID. Useful to find correlated customer event logs.
	 */
	private String correlationId;
	/**
	 * The response from the service.
	 */
	private String responseBodyText;
	/**
	 * List of HTTP-Response-Headers
	 */
	private HttpHeaders responseHeaders;

	protected ClientException(String msg) {
		super(msg);
	}

	protected ClientException(String msg, Throwable cause) {
		super(msg, cause);
	}

	protected ClientException(String msg, String responseBodyText, HttpHeaders responseHeaders){
		super(msg);
		this.responseBodyText = responseBodyText;
		this.responseHeaders = responseHeaders;
		this.correlationId = responseHeaders.getFirst("x-correlationID");
	}
}

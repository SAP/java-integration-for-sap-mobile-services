package com.sap.mobile.services.client.validation.broker.exception;

public class InstanceCreationTimeoutException extends Exception {
	public InstanceCreationTimeoutException() {
		super("Timeout while waiting for the instance to be created");
	}
}

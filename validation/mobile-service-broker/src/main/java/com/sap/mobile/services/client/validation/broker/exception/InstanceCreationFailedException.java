package com.sap.mobile.services.client.validation.broker.exception;

public class InstanceCreationFailedException extends Exception {
	public InstanceCreationFailedException() {
		super("Failed to create service instance");
	}
}

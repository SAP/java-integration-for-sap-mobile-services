package com.sap.mobile.services.client.validation.broker.exception;

public class MaxConcurrentInstancesReachedException extends Exception {

	public MaxConcurrentInstancesReachedException(final int maxInstances) {
		super(String.format("The maximum number of concurrent apps (%d) has been reached", maxInstances));
	}

}

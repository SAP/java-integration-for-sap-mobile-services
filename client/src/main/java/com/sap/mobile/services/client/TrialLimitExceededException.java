package com.sap.mobile.services.client;

public class TrialLimitExceededException extends ClientException {
	TrialLimitExceededException() {
		super("You exceeded your trial limit.");
	}
}

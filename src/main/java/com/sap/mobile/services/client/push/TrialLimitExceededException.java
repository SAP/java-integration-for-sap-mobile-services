package com.sap.mobile.services.client.push;

public class TrialLimitExceededException extends ClientException {
	TrialLimitExceededException() {
		super("You exceeded your trial limit.");
	}
}

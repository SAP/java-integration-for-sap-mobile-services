package com.sap.mobile.services.client;

public class TrialLimitExceededException extends ClientException {
	TrialLimitExceededException(String responseBodyText, com.sap.mobile.services.client.HttpHeaders httpHeaders) {
		super("You exceeded your trial limit.", responseBodyText, httpHeaders);
	}
}

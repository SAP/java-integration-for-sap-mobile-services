package com.sap.mobile.services.client;

import org.springframework.http.HttpHeaders;

public class TrialLimitExceededException extends ClientException {
	TrialLimitExceededException(String responseBodyText, HttpHeaders httpHeaders) {
		super("You exceeded your trial limit.", responseBodyText, httpHeaders);
	}
}

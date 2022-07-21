package com.sap.cloud.platform.mobile.services.validation;

import org.springframework.context.annotation.Bean;

import com.sap.mobile.services.client.push.PushClient;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PushClientProvider {

	private final PushClient cfBindingClient;
	private final PushClient x509Client;
	private final PushClient apiKeyClient;


	public PushClient defaultClient() {
		return cfBindingClient;
	}

}

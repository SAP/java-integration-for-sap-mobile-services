package com.sap.mobile.services.client.validation.broker.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlatformUserCredentials {

	private final String email;
	private final String password;

}

package com.sap.mobile.services.client;

import lombok.Getter;

@Getter
public class NoSuchTenantException extends ClientException {

	private final String tenantId;

	NoSuchTenantException(final String tenantId) {
		super(String.format("The tenant with id '%s' does not exist or is not subscribed to Mobile Services", tenantId));
		this.tenantId = tenantId;
	}
}

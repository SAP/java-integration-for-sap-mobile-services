package com.sap.mobile.services.client.validation.broker.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ServiceKeyRequest {

	@JsonProperty("serviceName")
	private String serviceName;

	@JsonProperty("scopes")
	private Set<String> scopes = new HashSet<>();

	@JsonProperty("x509")
	private boolean x509;

}

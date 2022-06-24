package com.sap.mobile.services.client.validation.broker.model;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceKey {

	@JsonProperty("url")
	private URI url;

	@JsonProperty("endpoints")
	private Map<String, EndpointConfig> endpoints = new HashMap<>();

	@JsonProperty("saasregistryenabled")
	private boolean saasRegistryEnabled;

	@JsonProperty("sap.cloud.service")
	private String sapCloudService;

	@JsonProperty("uaa")
	private UaaConfig uaa;

	public static class EndpointConfig {
		@JsonProperty("url")
		private URI url;

		@JsonProperty("timeout")
		private int timeout;
	}

	public static class UaaConfig {
		@JsonProperty("clientid")
		private String clientId;

		@JsonProperty("clientsecret")
		private String clientSecret;

		@JsonProperty("url")
		private URI url;

		@JsonProperty("identityzone")
		private String identityZone;

		@JsonProperty("identityzoneid")
		private String identityZoneId;

		@JsonProperty("tenantid")
		private String tenantId;

		@JsonProperty("tenantmode")
		private String tenantMode;

		@JsonProperty("sburl")
		private String sburl;

		@JsonProperty("apiurl")
		private URI apiUrl;

		@JsonProperty("verificationkey")
		private String verificationKey;

		@JsonProperty("xsappname")
		private String xsAppName;

		@JsonProperty("subaccountid")
		private String subaccountId;

		@JsonProperty("uaadomain")
		private String uaaDomain;

		@JsonProperty("zoneid")
		private String zoneId;

		@JsonProperty("credential-type")
		private String credentialType;
	}
}

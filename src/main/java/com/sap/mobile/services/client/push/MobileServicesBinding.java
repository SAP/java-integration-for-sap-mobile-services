package com.sap.mobile.services.client.push;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public final class MobileServicesBinding {

	public static MobileServicesBinding fromResource(String resource) throws IOException {
		try (InputStream is = MobileServicesBinding.class.getClassLoader().getResourceAsStream(resource)) {
			return fromInputStream(is);
		}
	}

	public static MobileServicesBinding fromInputStream(InputStream stream) throws IOException {
		return new ObjectMapper().readValue(stream, MobileServicesBinding.class);
	}

	@JsonProperty("endpoints")
	private Map<String, Endpoint> endpoints = new HashMap<>();
	@JsonProperty("sap.cloud.service")
	private String appName;
	@JsonProperty("uaa")
	private UaaConfig clientConfiguration;

	@Getter
	@Setter
	@NoArgsConstructor
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Endpoint {
		@JsonProperty("url")
		private String url;
		@JsonProperty("timeout")
		private int timeout;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@JsonIgnoreProperties(ignoreUnknown = true)
	// TODO: check if X509 support is already in place
	public static class UaaConfig {
		@JsonProperty("clientid")
		private String clientId;
		@JsonProperty("clientsecret")
		private String clientSecret;
		@JsonProperty("url")
		private String url;
		@JsonProperty("identityzone")
		private String identityZone;
	}
}

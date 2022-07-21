package com.sap.mobile.services.client.validation.broker.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class AppConfigOpenApiModel {

	@JsonProperty("server")
	private URI server;

	@JsonProperty("platform")
	private String platform;

	@JsonProperty("applicationId")
	private String applicationId;

	@JsonProperty("services")
	private List<AppService> services = new ArrayList<>();

	@Data
	public static class AppService {
		@JsonProperty("name")
		private String name;
		@JsonProperty("serviceKeys")
		private List<AbstractServiceKey> serviceKeys = new ArrayList<>();
	}


	@Data
	public static abstract class AbstractServiceKey {

		@JsonProperty("alias")
		private String alias;

		@JsonProperty("url")
		private URI server;

	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	public static class ApiKeyServiceKey extends AbstractServiceKey {
		@JsonProperty("apiKey")
		private String apiKey;
	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	public static class X509UaaServiceKey extends AbstractServiceKey {
		@JsonProperty("uaa")
		private ServiceKeyOpenApiModel.UaaConfig apiKey;
	}

}

package com.sap.mobile.services.client.validation.broker.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class AppConfig {

	@JsonProperty("server")
	private URI server;

	@JsonProperty("platform")
	private String platform;

	@JsonProperty("applicationId")
	private String applicationId;

	@JsonProperty("services")
	private List<AppService> services = new ArrayList<>();

	@Data
	@Builder
	public static class AppService {
		@JsonProperty("name")
		private String name;
		@JsonProperty("serviceKeys")
		private List<Map<?, ?>> serviceKeys = new ArrayList<>();
	}
}

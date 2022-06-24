package com.sap.mobile.services.client.validation.broker.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ServiceInstanceParams {

	@JsonProperty("name")
	private String name;

	@JsonProperty("displayName")
	private String displayName;

	@JsonProperty("security")
	private SecurityConfig security;

	@JsonProperty("services")
	@Builder.Default
	private List<Feature> features = new ArrayList<>();


	@Builder
	@Data
	public static class SecurityConfig {
		@JsonProperty("name")
		private String name;
	}

	@Builder
	@Data
	public static class Feature {
		@JsonProperty("name")
		private String name;

		@JsonProperty("parameters")
		private ObjectNode parameters;

	}

}

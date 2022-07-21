package com.sap.mobile.services.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.mobile.services.client.MobileServicesBinding.UaaConfig;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public final class MobileServicesSettings {

	public static MobileServicesSettings fromResource(String resource) throws IOException {
		try (InputStream is = MobileServicesSettings.class.getClassLoader().getResourceAsStream(resource)) {
			return fromInputStream(is);
		}
	}

	public static MobileServicesSettings fromInputStream(InputStream stream) throws IOException {
		return new ObjectMapper().readValue(stream, MobileServicesSettings.class);
	}

	private String server;
	private String applicationId;
	private Platform platform;
	private List<Service> services;

	public enum Platform {
		CF
	}

	@Getter
	@Setter
	@NoArgsConstructor
	public static class Service {
		private String name;
		private List<ServiceKey> serviceKeys;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@Builder(access = AccessLevel.PACKAGE)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ServiceKey {
		private String alias;
		private String url;
		private String apiKey;
		private UaaConfig uaa;
	}
}

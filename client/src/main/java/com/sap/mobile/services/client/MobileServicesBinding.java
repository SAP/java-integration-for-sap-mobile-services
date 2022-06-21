package com.sap.mobile.services.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public final class MobileServicesBinding {

	private static final String VCAP_SERVICES_NAME = "VCAP_SERVICES";
	private static final String MOBILE_SERVICES_TAG_PREFIX = "mobileservice";

	public static MobileServicesBinding fromResource(String resource) throws IOException {
		try (InputStream is = MobileServicesBinding.class.getClassLoader().getResourceAsStream(resource)) {
			return fromInputStream(is);
		}
	}

	public static MobileServicesBinding fromInputStream(InputStream stream) throws IOException {
		return new ObjectMapper().readValue(stream, MobileServicesBinding.class);
	}

	public static Optional<MobileServicesBinding> fromVCAPVariables() throws IOException {
		return fromVCAPVariables(System::getenv);
	}

	static Optional<MobileServicesBinding> fromVCAPVariables(final Function<String, String> environmentAccessor) throws IOException {
		final String vcapServices = environmentAccessor.apply(VCAP_SERVICES_NAME);
		if (vcapServices == null) {
			log.debug("No '{}' environment variable found", VCAP_SERVICES_NAME);
			return Optional.empty();
		}

		log.debug("Parsing bound services");
		final ObjectMapper objectMapper = new ObjectMapper();
		final VcapServices parsedService = objectMapper.readValue(vcapServices, VcapServices.class);
		final List<VcapService> serviceInstances = parsedService.values()
				.stream()
				.flatMap(List::stream)
				.filter(service -> service.tags.stream().anyMatch(tag -> tag.startsWith(MOBILE_SERVICES_TAG_PREFIX)))
				.collect(Collectors.toList());

		if (serviceInstances.isEmpty()) {
			log.info("No Mobile Services binding found");
			return Optional.empty();
		} else if (serviceInstances.size() > 1) {
			final String foundServiceNames = serviceInstances.stream().map(VcapService::getName).collect(Collectors.joining(", "));
			log.warn("Found multiple service bindings: {}", foundServiceNames);
			return Optional.empty();
		}

		final VcapService service = serviceInstances.get(0);
		return Optional.of(objectMapper.convertValue(service.getCredentials(), MobileServicesBinding.class));
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

	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class VcapServices extends HashMap<String, List<VcapService>> {
	}

	@Getter
	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class VcapService {
		@JsonProperty("name")
		private String name;
		@JsonProperty("tags")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		private Set<String> tags = new HashSet<>();
		@JsonProperty("credentials")
		private ObjectNode credentials;
	}
}

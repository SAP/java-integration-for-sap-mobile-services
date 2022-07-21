package com.sap.cloud.platform.mobile.services.validation.config;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.sap.cloud.platform.mobile.services.validation.PushClientProvider;
import com.sap.mobile.services.client.MobileServicesBinding;
import com.sap.mobile.services.client.MobileServicesSettings;
import com.sap.mobile.services.client.push.PushClient;
import com.sap.mobile.services.client.push.PushClientBuilder;

@Configuration
public class MobileServicesClientConfiguration {

	@Value("${service.binding.location}")
	private String serviceBindingLocation;

	@Value("${settings.apikey.location}")
	private String settingsApiKeyLocation;

	@Value("${settings.x509.location}")
	private String settingsX509Location;

	@Bean
	@Primary
	public PushClient cfBindingPushClient(final MobileServicesBinding binding) {
		return new PushClientBuilder().build(binding);
	}

	@Bean
	public PushClient apiKeyPushClient(@Qualifier("apiKeySettings") final MobileServicesSettings settings) {
		return new PushClientBuilder().build(settings);
	}

	@Bean
	public PushClient x509PushClient(@Qualifier("x509Settings") final MobileServicesSettings settings) {
		return new PushClientBuilder().build(settings);
	}

	@Bean
	public MobileServicesBinding mobileServicesBinding() throws IOException {
		try (final FileInputStream stream = new FileInputStream(serviceBindingLocation)) {
			return MobileServicesBinding.fromInputStream(stream);
		}
	}

	@Bean
	public MobileServicesSettings apiKeySettings() throws IOException {
		try (final FileInputStream stream = new FileInputStream(settingsApiKeyLocation)) {
			return MobileServicesSettings.fromInputStream(stream);
		}
	}

	@Bean
	public MobileServicesSettings x509Settings() throws IOException {
		try (final FileInputStream stream = new FileInputStream(settingsX509Location)) {
			return MobileServicesSettings.fromInputStream(stream);
		}
	}

	@Bean
	public PushClientProvider pushClientProvider() {
		return PushClientProvider.builder()
				.cfBindingClient(cfBindingPushClient(null))
				.x509Client(x509PushClient(null))
				.apiKeyClient(apiKeyPushClient(null))
				.build();
	}
}

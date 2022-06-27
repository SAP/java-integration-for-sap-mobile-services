package com.sap.cloud.platform.mobile.services.validation.config;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sap.mobile.services.client.MobileServicesBinding;
import com.sap.mobile.services.client.push.PushClient;
import com.sap.mobile.services.client.push.PushClientBuilder;

@Configuration
public class MobileServicesClientConfiguration {

	@Value("${service.binding.location}")
	private String serviceBindingLocation;

	@Bean
	public PushClient pushClient(final MobileServicesBinding binding) {
		return new PushClientBuilder().build(binding);
	}

	@Bean
	public MobileServicesBinding mobileServicesBinding() throws IOException {
		try (final FileInputStream stream = new FileInputStream(serviceBindingLocation)) {
			return MobileServicesBinding.fromInputStream(stream);
		}
	}
}

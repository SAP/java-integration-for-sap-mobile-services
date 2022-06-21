package com.sap.mobile.services.client.samples.cf.binding.configuration;

import java.io.IOException;

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sap.mobile.services.client.MobileServicesBinding;
import com.sap.mobile.services.client.push.PushClient;
import com.sap.mobile.services.client.push.PushClientBuilder;

@Configuration
public class MobileServicesConfiguration {

	@Bean
	public MobileServicesBinding mobileServicesBinding() throws IOException {
		return MobileServicesBinding.fromVCAPVariables()
				.orElseThrow(() -> new BeanInitializationException("No mobile-services binding found"));
	}

	@Bean
	public PushClient pushClient(final MobileServicesBinding mobileServicesBinding) {
		return new PushClientBuilder().build(mobileServicesBinding);
	}
}

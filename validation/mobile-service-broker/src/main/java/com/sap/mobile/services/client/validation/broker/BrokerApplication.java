package com.sap.mobile.services.client.validation.broker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.mobile.services.client.validation.broker.configuration.bean.MobileServicesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties(MobileServicesConfig.class)
public class BrokerApplication {

	@Bean
	ObjectMapper objectMapper() {
		return new ObjectMapper().findAndRegisterModules();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(BrokerApplication.class, args);
	}
	
}

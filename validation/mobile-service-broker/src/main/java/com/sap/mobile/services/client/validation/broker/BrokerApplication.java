package com.sap.mobile.services.client.validation.broker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.sap.mobile.services.client.validation.broker.configuration.bean.MobileServicesConfig;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties(MobileServicesConfig.class)
public class BrokerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrokerApplication.class, args);
	}

}

package com.sap.mobile.services.client.validation.broker.configuration.bean;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "broker")
@Getter
@Setter
public class MobileServicesConfig {
	private String serviceName = "mobile-services";
	private String planName = "standard";
	private String orgName;
	private String spaceName;
	private String namePrefix = "test-";
	private String credentialId = "platform-user";
	private int maxInstances = 15;
	private String brokerName;
	private Duration mobileApplicationLifetime = Duration.ofMinutes(20);
	private List<BrokerTemplateConfig> templates = new ArrayList<>();
}

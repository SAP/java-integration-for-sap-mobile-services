package com.sap.mobile.services.client.validation.broker.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.sap.mobile.services.client.validation.broker.configuration.bean.MobileServicesConfig;
import com.sap.mobile.services.client.validation.broker.model.PlatformUserCredentials;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CredentialProvider {

	private final CredentialStoreClient credentialStoreClient;
	private final MobileServicesConfig configuration;

	public PlatformUserCredentials getPlatformUserCredentials() {
		final String envEmail = System.getenv("BROKER_PLATFORM_USER_EMAIL");
		final String envPassword = System.getenv("BROKER_PLATFORM_USER_PASSWORD");
		if (StringUtils.isNotBlank(envEmail) && StringUtils.isNotBlank(envPassword)) {
			return PlatformUserCredentials.builder()
					.email(envEmail)
					.password(envPassword)
					.build();
		}

		final CredentialStoreClient.PasswordCredential credential = credentialStoreClient
				.findPasswordCredential(configuration.getCredentialId())
				.orElseThrow(() -> new IllegalStateException("Credential not found"));

		return PlatformUserCredentials.builder()
				.email(credential.getUsermame())
				.password(credential.getPassword())
				.build();
	}

}

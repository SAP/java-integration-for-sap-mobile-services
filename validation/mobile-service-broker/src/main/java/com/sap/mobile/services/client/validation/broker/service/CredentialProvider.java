package com.sap.mobile.services.client.validation.broker.service;

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
		final CredentialStoreClient.PasswordCredential credential = credentialStoreClient
				.findPasswordCredential(configuration.getCredentialId())
				.orElseThrow(() -> new IllegalStateException("Credential not found"));

		return PlatformUserCredentials.builder()
				.email(credential.getUsermame())
				.password(credential.getPassword())
				.build();
	}

}

package com.sap.mobile.services.client.validation.broker.configuration;

import java.net.MalformedURLException;
import java.net.URI;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sap.cloudfoundry.client.facade.CloudControllerClient;
import com.sap.cloudfoundry.client.facade.CloudControllerClientImpl;
import com.sap.cloudfoundry.client.facade.CloudCredentials;
import com.sap.cloudfoundry.client.facade.domain.CloudSpace;
import com.sap.mobile.services.client.validation.broker.configuration.bean.MobileServicesConfig;
import com.sap.mobile.services.client.validation.broker.model.PlatformUserCredentials;
import com.sap.mobile.services.client.validation.broker.service.CredentialProvider;

import io.pivotal.cfenv.core.CfEnv;

@Configuration
public class CFClientConfiguration {

	@Value("${vcap.application.organization_name:}")
	private String orgName;

	@Value("${vcap.application.space_name:}")
	private String spaceName;

	@Value("${vcap.application.cf_api:https://api.cf.sap.hana.ondemand.com}")
	private URI cfApi;

	@Bean
	public CloudSpace cfTarget(final CredentialProvider credentialProvider,
			final MobileServicesConfig config) throws MalformedURLException {
		final PlatformUserCredentials platformUserCredentials = credentialProvider.getPlatformUserCredentials();
		final CloudCredentials credentials = new CloudCredentials(platformUserCredentials.getEmail(), platformUserCredentials.getPassword());
		final CloudControllerClient baseClient = new CloudControllerClientImpl(cfApi.toURL(), credentials);

		baseClient.login();

		final String orgName = StringUtils.firstNonBlank(config.getOrgName(), this.orgName);
		final String spaceName = StringUtils.firstNonBlank(config.getSpaceName(), this.spaceName);

		if (StringUtils.isBlank(orgName)) {
			throw new BeanCreationException("No org name specified");
		} else if (StringUtils.isBlank(spaceName)) {
			throw new BeanCreationException("No space name specified");
		}

		return baseClient.getSpace(orgName, spaceName, true);
	}

	@Bean
	public CloudControllerClient cloudControllerClient(final CredentialProvider credentialProvider,
			final CloudSpace target) throws MalformedURLException {
		final PlatformUserCredentials platformUserCredentials = credentialProvider.getPlatformUserCredentials();
		final CloudCredentials credentials = new CloudCredentials(platformUserCredentials.getEmail(), platformUserCredentials.getPassword());
		final CloudControllerClient client = new CloudControllerClientImpl(cfApi.toURL(), credentials, target, false);

		client.login();
		return client;
	}

	@Bean
	public CfEnv cfEnv() {
		return new CfEnv();
	}


}

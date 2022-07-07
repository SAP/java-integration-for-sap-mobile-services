package com.sap.mobile.services.client.validation.broker.configuration;

import java.net.URI;

import org.apache.commons.lang3.StringUtils;
import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.v3.organizations.ListOrganizationsRequest;
import org.cloudfoundry.client.v3.organizations.ListOrganizationsResponse;
import org.cloudfoundry.client.v3.serviceplans.ListServicePlansRequest;
import org.cloudfoundry.client.v3.serviceplans.ListServicePlansResponse;
import org.cloudfoundry.client.v3.serviceplans.ServicePlanResource;
import org.cloudfoundry.client.v3.spaces.ListSpacesRequest;
import org.cloudfoundry.client.v3.spaces.ListSpacesResponse;
import org.cloudfoundry.client.v3.spaces.SpaceResource;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sap.mobile.services.client.validation.broker.configuration.bean.MobileServicesConfig;
import com.sap.mobile.services.client.validation.broker.model.PlatformUserCredentials;
import com.sap.mobile.services.client.validation.broker.service.CredentialProvider;

import io.pivotal.cfenv.core.CfEnv;
import reactor.core.publisher.Mono;

@Configuration
public class CFClientConfiguration {

	@Value("${vcap.application.organization_name:}")
	private String orgName;

	@Value("${vcap.application.space_name:}")
	private String spaceName;

	@Value("${vcap.application.cf_api:https://api.cf.sap.hana.ondemand.com}")
	private URI cfApi;

	@Bean
	public DefaultConnectionContext defaultCfConnectionContext() {
		return DefaultConnectionContext.builder()
				.apiHost(cfApi.getHost())
				.build();
	}

	@Bean
	public PasswordGrantTokenProvider cfTokenProvider(final CredentialProvider credentialProvider) {
		final PlatformUserCredentials platformUserCredentials = credentialProvider.getPlatformUserCredentials();
		return PasswordGrantTokenProvider.builder()
				.password(platformUserCredentials.getPassword())
				.username(platformUserCredentials.getEmail())
				.build();
	}

	@Bean
	public CloudFoundryClient cloudFoundryClient(final DefaultConnectionContext context,
			final PasswordGrantTokenProvider tokenProvider) {
		return ReactorCloudFoundryClient.builder()
				.tokenProvider(tokenProvider)
				.connectionContext(context)
				.build();
	}

	@Bean
	public SpaceResource spaceResource(final CloudFoundryClient client, final MobileServicesConfig config) {
		final String orgName = StringUtils.firstNonBlank(config.getOrgName(), this.orgName);
		final String spaceName = StringUtils.firstNonBlank(config.getSpaceName(), this.spaceName);

		if (StringUtils.isBlank(orgName)) {
			throw new BeanCreationException("No org name specified");
		} else if (StringUtils.isBlank(spaceName)) {
			throw new BeanCreationException("No space name specified");
		}

		return client.organizationsV3().list(ListOrganizationsRequest.builder()
						.name(orgName)
						.build()
				).map(ListOrganizationsResponse::getResources)
				.flatMap(orgs -> orgs.size() > 0 ? Mono.just(orgs.get(0)) : Mono.empty())
				.flatMap(org -> {
					return client.spacesV3().list(ListSpacesRequest.builder()
							.organizationId(org.getId())
							.name(spaceName)
							.build());
				}).map(ListSpacesResponse::getResources)
				.flatMap(spaces -> spaces.size() > 0 ? Mono.just(spaces.get(0)) : Mono.empty())
				.blockOptional()
				.orElseThrow(() -> {
					return new BeanInitializationException(String.format("Failed to find locate space (%s/%s)", orgName, spaceName));
				});
	}

	@Bean
	public ServicePlanResource servicePlanResource(final CloudFoundryClient client, final MobileServicesConfig config,
			final SpaceResource spaceResource) {
		final ListServicePlansRequest.Builder builder = ListServicePlansRequest.builder()
				.name(config.getPlanName())
				.serviceOfferingName(config.getServiceName());
		if (StringUtils.isNotBlank(config.getBrokerName())) {
			builder.serviceBrokerName(config.getBrokerName());
		}

		return client.servicePlansV3().list(builder.build())
				.map(ListServicePlansResponse::getResources)
				.flatMap(plans -> plans.size() > 0 ? Mono.just(plans.get(0)) : Mono.empty())
				.blockOptional()
				.orElseThrow(() -> {
					return new BeanInitializationException(String.format("Failed to find locate service plan (%s/%s)", config.getServiceName(), config.getPlanName()));
				});
	}

	@Bean
	public CfEnv cfEnv() {
		return new CfEnv();
	}


}

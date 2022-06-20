package com.sap.mobile.services.client.push;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.sap.cloud.security.xsuaa.tokenflows.ClientCredentialsTokenFlow;
import com.sap.mobile.services.client.BuildProperties;
import com.sap.mobile.services.client.ClientConfiguration;
import com.sap.mobile.services.client.CustomAuthClientConfiguration;
import com.sap.mobile.services.client.CustomAuthHeaderSupplier;
import com.sap.mobile.services.client.MobileServicesBinding;
import com.sap.mobile.services.client.MobileServicesSettings;
import com.sap.mobile.services.client.ServiceKeyClientConfiguration;
import com.sap.mobile.services.client.XsuaaClientConfiguration;
import com.sap.mobile.services.client.XsuaaTokenFlowFactory;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
/**
 * The PushClientBuilder is used to instantiate a push client from a
 * configuration source.
 */
public final class PushClientBuilder {

	private Duration connectTimeout;
	private Duration readTimeout;
	private XsuaaTokenFlowFactory tokenFlowFactory = new XsuaaTokenFlowFactory();
	private Supplier<Optional<String>> tenantResolver = Optional::empty;

	/**
	 * Instantiate the push client from a mobile service setting source, using the
	 * first push service key alias
	 *
	 * @param settings
	 * @return
	 */
	public PushClient build(MobileServicesSettings settings) {
		return this.build(settings, null);
	}

	/**
	 * Instantiate the push client from a mobile service setting source, using the
	 * given push service key alias
	 *
	 * @param settings
	 * @return
	 */
	public PushClient build(MobileServicesSettings settings, String serviceKeyAlias) {
		Assert.notNull(settings, "MobileServicesSettings must not be null.");

		final String applicationId = settings.getApplicationId();
		final List<MobileServicesSettings.ServiceKey> pushServiceKeys = settings.getServices().stream()
				.filter(s -> "push".equalsIgnoreCase(s.getName())).findFirst()
				.map(MobileServicesSettings.Service::getServiceKeys).orElseThrow(() -> new IllegalArgumentException(
						"Missing push service in mobile services configuration file."));

		final MobileServicesSettings.ServiceKey serviceKey;
		if (pushServiceKeys.isEmpty()) {
			throw new IllegalArgumentException("No service keys for push defined in configuration file.");
		}
		if (!StringUtils.hasLength(serviceKeyAlias)) {
			if (pushServiceKeys.size() > 1) {
				throw new IllegalArgumentException(
						"No specific service key alias provided but more than one service keys for push are present. Please provide a name.");
			}
			serviceKey = pushServiceKeys.get(0);
		} else {
			serviceKey = pushServiceKeys.stream().filter(sk -> serviceKeyAlias.equals(sk.getAlias())).findFirst()
					.orElseThrow(() -> new IllegalArgumentException(
							String.format("Service key with name %s not found in configuration file.",
									serviceKeyAlias)));
		}

		ClientConfiguration clientConfiguration = ServiceKeyClientConfiguration.builder()
				.buildProperties(BuildProperties.getInstance())
				.connectTimeout(this.connectTimeout).readTimeout(this.readTimeout).applicationId(applicationId)
				.tenantResolver(this.tenantResolver).serviceKey(serviceKey).build();

		return new RestTemplatePushClient(clientConfiguration);
	}

	/**
	 * Instantiate the push client from a mobile service binding source.
	 *
	 * @param binding
	 * @return
	 */
	public PushClient build(MobileServicesBinding binding) {
		Assert.notNull(binding, "MobileServicesBinding must not be null.");

		final String applicationId = binding.getAppName();
		final ClientCredentialsTokenFlow tokenFlow = tokenFlowFactory.createClientCredentialsTokenFlow(binding);

		final MobileServicesBinding.Endpoint endpoint = Optional
				.ofNullable(binding.getEndpoints().get(Constants.Binding.MOBILE_SERVICES_ENDPOINT_NAME))
				.orElseThrow(() -> new IllegalArgumentException(
						"Missing endpoint in mobile services binding."));

		final Duration endpointReadTimeout = endpoint.getTimeout() > 0 ? Duration.ofMillis(endpoint.getTimeout())
				: null;
		final XsuaaClientConfiguration clientConfiguration = XsuaaClientConfiguration.builder()
				.buildProperties(BuildProperties.getInstance())
				.connectTimeout(this.connectTimeout)
				.readTimeout(Optional.ofNullable(this.readTimeout).orElse(endpointReadTimeout))
				.applicationId(applicationId)
				.tenantResolver(this.tenantResolver)
				.rootUri(endpoint.getUrl())
				.xsuaaTokenFlow(tokenFlow)
				.build();

		return new RestTemplatePushClient(clientConfiguration);
	}

	/**
	 * Instantiate the push client with custom authorization mechanisms.
	 * This is meant to be a fallback if any other method will not work for the use case.
	 *
	 * @param applicationId
	 * @param rootUri
	 * @param authHeaderSupplier
	 */
	public PushClient build(String applicationId, String rootUri, CustomAuthHeaderSupplier authHeaderSupplier) {
		Assert.notNull(applicationId, "applicationId must not be null.");
		Assert.notNull(rootUri, "rootUri must not be null.");
		Assert.notNull(authHeaderSupplier, "authHeaderSupplier must not be null.");

		ClientConfiguration clientConfiguration = CustomAuthClientConfiguration.builder()
				.buildProperties(BuildProperties.getInstance())
				.connectTimeout(this.connectTimeout).readTimeout(this.readTimeout).applicationId(applicationId)
				.tenantResolver(this.tenantResolver).rootUri(rootUri).authHeaderSupplier(authHeaderSupplier).build();

		return new RestTemplatePushClient(clientConfiguration);
	}

	/**
	 * Configure the client's connect timeout.
	 *
	 * @param connectTimeout client connect timeout
	 * @return new instance of client builder
	 */
	public PushClientBuilder withConnectTimeout(Duration connectTimeout) {
		return new PushClientBuilder(connectTimeout, this.readTimeout, this.tokenFlowFactory, this.tenantResolver);
	}

	/**
	 * Configure the client's read timeout.
	 *
	 * @param readTimeout client read timeout
	 * @return new instance of client builder
	 */
	public PushClientBuilder withReadTimeout(Duration readTimeout) {
		return new PushClientBuilder(this.connectTimeout, readTimeout, this.tokenFlowFactory, this.tenantResolver);
	}

	/**
	 * Configure the client's tenant resolver.
	 *
	 * @param tenantResolver tenant resolver
	 * @return new instance of client builder
	 */
	public PushClientBuilder withTenantResolver(Supplier<Optional<String>> tenantResolver) {
		return new PushClientBuilder(this.connectTimeout, this.readTimeout, this.tokenFlowFactory, tenantResolver);
	}

	/**
	 * Configure the client's tenant-id.
	 *
	 * @param tenantId tenant-id
	 * @return new instance of client builder
	 */
	public PushClientBuilder withTenantId(String tenantId) {
		return withTenantResolver(() -> Optional.of(tenantId));
	}

	/**
	 * Configure the client's token flows factory.
	 * Only for internal usage.
	 *
	 * @param tokenFlowFactory token flows factory
	 * @return new instance of client builder
	 */
	PushClientBuilder withTokenFlowFactory(final XsuaaTokenFlowFactory tokenFlowFactory) {
		Assert.notNull(tokenFlowFactory, "XsuaaTokenFlowFactory must not be null.");
		return new PushClientBuilder(this.connectTimeout, this.readTimeout, tokenFlowFactory, this.tenantResolver);
	}

	public static final class PushClientBuilderException extends RuntimeException {
		private static final long serialVersionUID = 877946818344983395L;

		private PushClientBuilderException(String msg) {
			super(msg);
		}
	}
}

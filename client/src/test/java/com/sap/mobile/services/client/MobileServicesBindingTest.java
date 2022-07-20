package com.sap.mobile.services.client;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class MobileServicesBindingTest {

	private Map<String, String> envVariables;
	private Function<String, String> environmentAccessor;

	@Before
	public void setUp() {
		envVariables = new HashMap<>();
		environmentAccessor = envVariables::get;
	}

	@Test
	public void testBinding() throws Exception {
		MobileServicesBinding settings = MobileServicesBinding.fromResource("mobileservices-binding.json");
		assertThat(settings.getAppName(), is("appName"));
		assertThat(settings.getEndpoints(), hasKey("mobileservices"));
		assertThat(settings.getEndpoints().get("mobileservices").getTimeout(), is(160000));
		assertThat(settings.getEndpoints().get("mobileservices").getUrl(),
				is("https://anyUrl"));
		assertThat(settings.getClientConfiguration().getClientId(),
				is("clientId"));
		assertThat(settings.getClientConfiguration().getClientSecret(), is("clientSecret"));
		assertThat(settings.getClientConfiguration().getIdentityZone(), is("identityZone"));
		assertThat(settings.getClientConfiguration().getUrl(),
				is("uaaUrl"));
	}

	@Test
	public void testBindingVcapEmpty() throws Exception {
		String vcapServices = IOUtils.resourceToString("/vcap/vcap-services-empty.json", StandardCharsets.UTF_8);
		envVariables.put("VCAP_SERVICES", vcapServices);

		assertThat(MobileServicesBinding.fromVCAPVariables(environmentAccessor), is(Optional.empty()));
	}

	@Test
	public void testBindingVcapNoMobileServices() throws Exception {
		String vcapServices = IOUtils.resourceToString("/vcap/vcap-services-no-mobile-services.json", StandardCharsets.UTF_8);
		envVariables.put("VCAP_SERVICES", vcapServices);

		assertThat(MobileServicesBinding.fromVCAPVariables(environmentAccessor), is(Optional.empty()));
	}

	@Test
	public void testBindingVcapSingleMatch() throws Exception {
		String vcapServices = IOUtils.resourceToString("/vcap/vcap-services-single-match.json", StandardCharsets.UTF_8);
		envVariables.put("VCAP_SERVICES", vcapServices);

		Optional<MobileServicesBinding> result = MobileServicesBinding.fromVCAPVariables(environmentAccessor);
		assertThat(result.isPresent(), is(true));

		MobileServicesBinding binding = result.get();
		assertThat(binding.getAppName(), is("appName"));
		assertThat(binding.getEndpoints(), hasKey("mobileservices"));
		assertThat(binding.getEndpoints().get("mobileservices").getTimeout(), is(160000));
		assertThat(binding.getEndpoints().get("mobileservices").getUrl(),
				is("https://anyUrl"));
		assertThat(binding.getClientConfiguration().getClientId(),
				is("clientId"));
		assertThat(binding.getClientConfiguration().getClientSecret(), is("clientSecret"));
		assertThat(binding.getClientConfiguration().getIdentityZone(), is("identityZone"));
		assertThat(binding.getClientConfiguration().getUrl(),
				is("uaaUrl"));
	}

	@Test
	public void testBindingVcapMultipleMatches() throws Exception {
		String vcapServices = IOUtils.resourceToString("/vcap/vcap-services-multiple-matches.json", StandardCharsets.UTF_8);
		envVariables.put("VCAP_SERVICES", vcapServices);

		assertThat(MobileServicesBinding.fromVCAPVariables(environmentAccessor), is(Optional.empty()));
	}
}

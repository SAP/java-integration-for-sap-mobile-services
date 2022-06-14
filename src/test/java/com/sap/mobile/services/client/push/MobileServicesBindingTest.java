/**
 * (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * No part of this publication may be reproduced or transmitted in any form or for any purpose
 * without the express permission of SAP SE. The information contained herein may be changed
 * without prior notice.
 */

package com.sap.mobile.services.client.push;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class MobileServicesBindingTest {

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
}

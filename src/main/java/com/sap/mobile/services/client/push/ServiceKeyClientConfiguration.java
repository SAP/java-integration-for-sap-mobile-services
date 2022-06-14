/**
 * (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * No part of this publication may be reproduced or transmitted in any form or for any purpose
 * without the express permission of SAP SE. The information contained herein may be changed
 * without prior notice.
 */

package com.sap.mobile.services.client.push;

import org.springframework.http.client.ClientHttpRequestInterceptor;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter(AccessLevel.PACKAGE)
@SuperBuilder
class ServiceKeyClientConfiguration extends ClientConfiguration {
	private MobileServicesSettings.ServiceKey serviceKey;

	@Override
	String getRootUri() {
		return serviceKey.getUrl();
	}

	@Override
	ClientHttpRequestInterceptor getAuthInterceptor() {
		return new ApiKeyAuthorizationRequestInterceptor(this.serviceKey);
	}

}

/**
 * (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * No part of this publication may be reproduced or transmitted in any form or for any purpose
 * without the express permission of SAP SE. The information contained herein may be changed
 * without prior notice.
 */

package com.sap.mobile.services.client.push;

import org.springframework.http.client.ClientHttpRequestInterceptor;

import com.sap.cloud.security.xsuaa.tokenflows.ClientCredentialsTokenFlow;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter(AccessLevel.PACKAGE)
@SuperBuilder
class XsuaaClientConfiguration extends ClientConfiguration {
	private String rootUri;
	private ClientCredentialsTokenFlow xsuaaTokenFlow;

	@Override
	ClientHttpRequestInterceptor getAuthInterceptor() {
		return new XsuaaAuthorizationRequestInterceptor(xsuaaTokenFlow);
	}

}

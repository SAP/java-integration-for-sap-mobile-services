/**
 * (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * No part of this publication may be reproduced or transmitted in any form or for any purpose
 * without the express permission of SAP SE. The information contained herein may be changed
 * without prior notice.
 */

package com.sap.mobile.services.client.push;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.http.client.ClientHttpRequestInterceptor;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter(AccessLevel.PACKAGE)
@SuperBuilder
abstract class ClientConfiguration {
	private BuildProperties buildProperties;

	private String applicationId;

	private Duration connectTimeout;
	private Duration readTimeout;
	private Supplier<Optional<String>> tenantResolver;

	abstract String getRootUri();

	abstract ClientHttpRequestInterceptor getAuthInterceptor();
}

/**
 * (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * No part of this publication may be reproduced or transmitted in any form or for any purpose
 * without the express permission of SAP SE. The information contained herein may be changed
 * without prior notice.
 */

package com.sap.mobile.services.client.push;

import lombok.Getter;

@Getter
public class NoSuchTenantException extends ClientException {

	private final String tenantId;

	NoSuchTenantException(final String tenantId) {
		super(String.format("The tenant with id '%s' does not exist or is not subscribed to Mobile Services", tenantId));
		this.tenantId = tenantId;
	}
}

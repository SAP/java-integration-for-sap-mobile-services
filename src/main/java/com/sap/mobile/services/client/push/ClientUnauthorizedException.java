/**
 * (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * No part of this publication may be reproduced or transmitted in any form or for any purpose
 * without the express permission of SAP SE. The information contained herein may be changed
 * without prior notice.
 */

package com.sap.mobile.services.client.push;

/**
 * Thrown when authorization failed. Please check and update the client configuration.
 */
public class ClientUnauthorizedException extends ClientException {

	ClientUnauthorizedException() {
		super("Authorization failed.");
	}
}

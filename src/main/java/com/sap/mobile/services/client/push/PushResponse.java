/**
 * (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * No part of this publication may be reproduced or transmitted in any form or for any purpose
 * without the express permission of SAP SE. The information contained herein may be changed
 * without prior notice.
 */

package com.sap.mobile.services.client.push;

import java.util.List;


/**
 * The generic response for all notification methods
 */
public interface PushResponse {
	/**
	 * The generic notification method result
	 */
	StatusResponseStatus getStatus();

	/** List of notification results */
	List<? extends PushResult> getResults();
}

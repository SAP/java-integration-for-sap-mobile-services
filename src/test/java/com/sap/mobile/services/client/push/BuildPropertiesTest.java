/**
 * (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * No part of this publication may be reproduced or transmitted in any form or for any purpose
 * without the express permission of SAP SE. The information contained herein may be changed
 * without prior notice.
 */

package com.sap.mobile.services.client.push;

import org.junit.Assert;
import org.junit.Test;

public class BuildPropertiesTest {

	@Test
	public void testBuildVersion() throws Exception {
		BuildProperties props = BuildProperties.getInstance();
		Assert.assertNotNull(props.getVersion());
	}
}

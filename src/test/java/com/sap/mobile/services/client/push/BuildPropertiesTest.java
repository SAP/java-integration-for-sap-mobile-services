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

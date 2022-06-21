package com.sap.mobile.services.client;

import org.junit.Assert;
import org.junit.Test;

import com.sap.mobile.services.client.BuildProperties;

public class BuildPropertiesTest {

	@Test
	public void testBuildVersion() throws Exception {
		BuildProperties props = BuildProperties.getInstance();
		Assert.assertNotNull(props.getVersion());
	}
}

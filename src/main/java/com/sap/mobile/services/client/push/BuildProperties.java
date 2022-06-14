/**
 * (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * No part of this publication may be reproduced or transmitted in any form or for any purpose
 * without the express permission of SAP SE. The information contained herein may be changed
 * without prior notice.
 */

package com.sap.mobile.services.client.push;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

final class BuildProperties {

	private static final String BUILD_INFO_FILE = "META-INF/build-info.properties";
	private static BuildProperties INSTANCE = null;

	static BuildProperties getInstance() {
		if (BuildProperties.INSTANCE == null) {
			BuildProperties.INSTANCE = new BuildProperties();
		}
		return BuildProperties.INSTANCE;
	}

	private final Properties properties;

	private BuildProperties() throws BuildPropertiesInitializationException {
		try (InputStream is = getClass().getClassLoader().getResourceAsStream(BUILD_INFO_FILE)) {
			this.properties = new Properties();
			this.properties.load(is);
		} catch (IOException e) {
			throw new BuildPropertiesInitializationException(e);
		}
	}

	String getVersion() {
		return this.properties.getProperty("build.version");
	}

	static class BuildPropertiesInitializationException extends RuntimeException {

		private static final long serialVersionUID = 6636928634476765909L;

		BuildPropertiesInitializationException(Throwable cause) {
			super(cause);
		}
	}
}

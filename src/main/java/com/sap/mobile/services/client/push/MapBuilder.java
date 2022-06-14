/**
 * (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * No part of this publication may be reproduced or transmitted in any form or for any purpose
 * without the express permission of SAP SE. The information contained herein may be changed
 * without prior notice.
 */

package com.sap.mobile.services.client.push;

import java.util.HashMap;
import java.util.Map;

class MapBuilder<V> {

	static <V> MapBuilder<V> newInstance(Class<V> clazz) {
		return new MapBuilder<>();
	}

	private final Map<String, V> map;

	private MapBuilder() {
		this.map = new HashMap<>();
	}

	MapBuilder<V> put(String key, V value) {
		this.map.put(key, value);
		return this;
	}

	Map<String, V> build() {
		return this.map;
	}

	Map<String, V> putAndBuild(String key, V value) {
		return this.put(key, value).build();
	}
}

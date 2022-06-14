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

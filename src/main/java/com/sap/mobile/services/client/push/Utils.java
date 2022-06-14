/**
 * (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * No part of this publication may be reproduced or transmitted in any form or for any purpose
 * without the express permission of SAP SE. The information contained herein may be changed
 * without prior notice.
 */

package com.sap.mobile.services.client.push;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

final class Utils {

	private Utils() {
	}

	static <T, U> U safeMap(T value, Function<T, U> mapper) {
		return Optional.ofNullable(value).map(mapper).orElse(null);
	}

	static <T, U, V extends Collection<U>> V safeMapCollection(Collection<T> value, Function<T, U> mapper,
			Collector<U, ?, V> collector) {
		return safeMap(value, v -> v.stream().map(mapper).collect(collector));
	}

	static <T, U> List<U> safeMapList(Collection<T> value, Function<T, U> mapper) {
		return safeMapCollection(value, mapper, Collectors.toList());
	}

	static <T, U> Set<U> safeMapSet(Collection<T> value, Function<T, U> mapper) {
		return safeMapCollection(value, mapper, Collectors.toSet());
	}

	static <K, V, Kn, Vn> Map<Kn, Vn> safeMapMap(Map<K, V> value, Function<K, Kn> keyMapper, Function<V, Vn> valueMapper) {
		return safeMap(value, map -> {
			Map<Kn, Vn> newMap = new HashMap<>();
			map.forEach((k, v) -> {
				newMap.put(keyMapper.apply(k), valueMapper.apply(v));
			});
			return newMap;
		});
	}
}

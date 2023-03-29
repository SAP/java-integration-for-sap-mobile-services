package com.sap.mobile.services.client;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
/**
 * Represents a list of Http Headers with values
 */
@Getter
@Setter(AccessLevel.PACKAGE)
public class HttpHeaders {
	private Map<String,List<String>> headers;

	public String getFirst(String name){
		List <String> headerValues = headers.getOrDefault(name, Collections.emptyList());
		if (!headerValues.isEmpty()){
			return headerValues.get(0);
		}
		return null;
	}
}
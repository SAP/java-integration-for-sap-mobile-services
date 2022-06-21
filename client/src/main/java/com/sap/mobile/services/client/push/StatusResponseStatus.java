package com.sap.mobile.services.client.push;

import java.util.Map;

/**
 * The generic notification method result. This is used only when all notification returns the same completion code.
 */
public interface StatusResponseStatus {
	/** The status code. */
	Integer getCode();

	/** Status message. */
	String getMessage();

	/** Additional completion information. */
	Map<String, String> getParameters();
}

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

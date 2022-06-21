package com.sap.mobile.services.client;

/**
 * Represents a simple HTTP header
 */
public interface HttpHeader {

	/**
	 * Get the name of the header.
	 *
	 * @return name
	 */
	String getName();

	/**
	 * Get the value of the header.
	 *
	 * @return value
	 */
	String getValue();
}

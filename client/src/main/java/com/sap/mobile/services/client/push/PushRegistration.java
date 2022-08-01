package com.sap.mobile.services.client.push;

/**
 * Represents a push registration in a mobile application.
 */
public interface PushRegistration {

	/**
	 * Push registration ID.
	 *
	 * @return ID
	 */
	String getId();

	/**
	 * The unique device ID, used when sending a notification to a specific device.
	 *
	 * @return deviceId
	 */
	String getDeviceId();

	/**
	 * The device model, provided by the device during registration.
	 *
	 * @return device model
	 */
	String getDeviceModel();

	/**
	 * The device's form factor: phone, tablet or browser
	 *
	 * @return form factor
	 */
	FormFactor getFormFactor();

	/**
	 * The push group.
	 *
	 * @return push group
	 */
	String getGroup();

	/**
	 * The push provider.
	 *
	 * @return push provider
	 */
	Provider getProvider();

	/**
	 * The device locale.
	 *
	 * @return locale
	 */
	String getLocale();

	/**
	 * The user name.
	 *
	 * @return username
	 */
	String getUsername();
}

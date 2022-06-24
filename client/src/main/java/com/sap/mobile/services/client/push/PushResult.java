package com.sap.mobile.services.client.push;

/**
 * The information of each notification handed over to a push gateway
 */
public interface PushResult {

	/** The device registration ID */
	String getTarget();

	/**
	 * The notification ID. This can be used to retrieve the current status of the
	 * notification via {@link PushClient#getNotificationStatus(String)}
	 */
	String getNotificationId();

	/**
	 * the completion code. Possible values are
	 * <ul>
	 * <li>-1 - Notification was queued. The communication with the native push
	 * provider failed.</li>
	 * <li>0 - Notification was sent.</li>
	 * <li>1 - Notification was rejected because of invalid data.
	 * <li>2 - Notification was rejected because the target type was not found /
	 * invalid.</li>
	 * <li>3 - Notification could not be queued. See error details.</li>
	 * <li>4 - The notification configuration has expired.</li>
	 * <li>5 - The push token in the registration is missing. The device must obtain
	 * a new push token.</li>
	 * <li>6 - The device token was rejected by the native push provider.</li>
	 * <li>7 - The device registration has expired and the device must register
	 * again.</li>
	 * <li>8 - Too many notifications were sent to the device and the notification
	 * was rejected. Notifications will resume once the number is reduced.</li>
	 * <li>9 - The native notification is too large. Reduce the notification
	 * size.</li>
	 * <li>10 - Notification was not accepted by the native push provider. See the
	 * notification for details.</li>
	 * <li>11 - Recoverable IO problem while sending the notification to the native
	 * push provider. The notification was queued.</li>
	 * <li>12 and 13 - The communication with the native push provider failed. The
	 * notification was not queued. Please retry later and raise a support ticket
	 * with the full request and response, including headers and body.</li>
	 * <li>14 - The notification quota for the push provider has been exceeded.
	 * Reduce the number of notifications sent by the back-end for this native push
	 * provider.</li>
	 * </ul>
	 */
	Integer getCode();

	/** Status text */
	String getMessage();
}

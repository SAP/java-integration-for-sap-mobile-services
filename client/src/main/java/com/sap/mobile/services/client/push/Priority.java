package com.sap.mobile.services.client.push;

/**
 * Global notification priority. The notification priority affects APNS and FCM
 * notifications.
 * APNS background {@link ApnsNotification#getContentAvailable()} notifications
 * will override the priority with NORMAL
 */
public enum Priority {
	NORMAL, HIGH
}

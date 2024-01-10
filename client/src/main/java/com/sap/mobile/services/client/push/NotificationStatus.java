package com.sap.mobile.services.client.push;

import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Notification status detail response element of
 * {@link NotificationStatusResponse}
 */
public interface NotificationStatus {

	/** Notification status. */
	Status getStatus();

	/** The notification producer. */
	String getCaller();

	/** The target type, like ios, android ... */
	String getNotificationType();

	/**
	 * List of matching topic subscriptions on
	 * {@link PushClient#pushToTopics(java.util.Collection, java.util.Collection, LocalizedPushPayload)}
	 */
	Topics getTopics();

	/**
	 * Native notification ID from channel, like APNs or FCM.
	 * APNs apns-unique-id is used on development target.
	 */
	String getNotificationId();

	/**
	 * The current notification status.
	 * <p>
	 * Please note: Some status relies on the mobile app implementing callbacks and
	 * may not be available.
	 */
	public enum Status {
		/** The notification is queued and send later. */
		QUEUED,
		/** The notification was accepted by the push provider. */
		SENT,
		/** Sending the notification failed. */
		ERROR,
		/**
		 * The mobile app has received the notification. This status relies on the app
		 * implementation.
		 */
		RECEIVED,
		/**
		 * The mobile app has consumed the notification. This status relies on the app
		 * implementation.
		 */
		CONSUMED,
		/**
		 * The mobile app has confirmed the notification. This status relies on the app
		 * implementation.
		 */
		CONFIRMED;

		@JsonCreator
		public static Status fromString(String val) {
			if (val == null) {
				return null;
			}
			return Status.valueOf(val.toUpperCase(Locale.US));
		}
	}

}

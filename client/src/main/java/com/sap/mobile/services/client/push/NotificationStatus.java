package com.sap.mobile.services.client.push;

import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonCreator;

public interface NotificationStatus {

	/**  Notification status. */
	Status getStatus();

	/** The notification producer. */
	String getCaller();

	/** The target type, like ios, android ... */
	String getNotificationType();

	public enum Status {
		/** The notification is queued and send later. */
		QUEUED,
		/** The notification was accepted by the push provider. */
		SENT,
		/** Sending the notification failed. */
		ERROR,
		/** The mobile app has received the notification. This status relies on the app implementation. */
		RECEIVED,
		/** The mobile app has consumed the notification. This status relies on the app implementation. */
		CONSUMED,
		/** The mobile app has confirmed the notification. This status relies on the app implementation. */
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

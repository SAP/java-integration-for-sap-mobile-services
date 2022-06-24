package com.sap.mobile.services.client.push;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * User specific payload element for server localized bulk push
 */
public interface LocalizedUserNotification {

	static Builder builder() {
		return new Builder();
	}

	String getUser();

	LocalizedPushPayload getNotification();

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	final class Builder {
		private String user;
		private LocalizedPushPayload notification;

		/**
		 * Specifies the recipient.
		 * 
		 * @param user
		 * @return
		 */
		public Builder user(String user) {
			return new Builder(user, this.notification);
		}

		/**
		 * Specify the server localized push payload.
		 * 
		 * @param notification
		 * @return
		 */
		public Builder notification(LocalizedPushPayload notification) {
			return new Builder(this.user, notification);
		}

		/**
		 * Generate the push to single user payload.
		 * 
		 * @return
		 */
		public LocalizedUserNotification build() {
			return new LocalizedUserNotificationObject(this.user, this.notification);
		}

		@Getter
		@RequiredArgsConstructor
		private static class LocalizedUserNotificationObject implements LocalizedUserNotification {
			private final String user;
			private final LocalizedPushPayload notification;
		}
	}
}

package com.sap.mobile.services.client.push;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * Representation of the bulk push user request element. Use {@link Builder} to
 * define and build the bulk push user request element.
 */
public interface UserNotification {

	static Builder builder() {
		return new Builder();
	}

	String getUser();

	PushPayload getNotification();

	/**
	 * Bulk push notification payload for a specific user.
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	final class Builder {
		private String user;
		private PushPayload notification;

		/** Username */
		public Builder user(String user) {
			return new Builder(user, this.notification);
		}

		/** Specific push payload */
		public Builder notification(PushPayload notification) {
			return new Builder(this.user, notification);
		}

		public UserNotification build() {
			return new UserNotificationObject(this.user, this.notification);
		}

		@Getter
		@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
		private static class UserNotificationObject implements UserNotification {
			private final String user;
			private final PushPayload notification;
		}
	}
}

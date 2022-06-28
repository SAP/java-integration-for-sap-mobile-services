package com.sap.mobile.services.client.push;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * User specific bulk push payload element for server localized bulk push @see
 * <a target="_top" href=
 * "https://help.sap.com/doc/f53c64b93e5140918d676b927a3cd65b/Cloud/en-US/docs-en/guides/features/push/api/localization.html#push-service-based-localization">Push
 * Service Based Localization</a>. Use the {@link Builder} to define and build
 * the request.
 */
public interface LocalizedUserNotification {

	static Builder builder() {
		return new Builder();
	}

	String getUser();

	LocalizedPushPayload getNotification();

	/**
	 * Helper for user specific bulk push payload creation.
	 */
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

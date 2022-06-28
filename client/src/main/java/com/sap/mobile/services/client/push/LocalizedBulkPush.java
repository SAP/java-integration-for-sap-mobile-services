package com.sap.mobile.services.client.push;

import java.util.Collection;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * Root payload element for push service localized bulk
 * push @see<a target="_top" href=
 * "https://help.sap.com/doc/f53c64b93e5140918d676b927a3cd65b/Cloud/en-US/docs-en/guides/features/push/api/localization.html#push-service-based-localization">
 * Push Service Based Localization</a>. Use the {@link Builder} to define and
 * build the element.
 */
public interface LocalizedBulkPush {

	static Builder builder() {
		return new Builder();
	}

	LocalizedPushPayload getNotification();

	Collection<LocalizedUserNotification> getUserNotifications();

	/**
	 * Helper for server localized bulk push payload creation.
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	final class Builder {
		private LocalizedPushPayload notification;
		private Collection<LocalizedUserNotification> userNotifications;

		/**
		 * Sets the server localized notifications for all registered devices.
		 * 
		 * @param notification
		 * @return a configured builder with notification for all registered devices.
		 */
		public Builder notification(LocalizedPushPayload notification) {
			return new Builder(notification, this.userNotifications);
		}

		/**
		 * Sets the server localized notification element for users specific
		 * notifications.
		 * 
		 * @param userNotifications
		 * @return a configured builder with user specific notifications.
		 */
		public Builder userNotifications(Collection<LocalizedUserNotification> userNotifications) {
			return new Builder(this.notification, userNotifications);
		}

		/**
		 * @return the server localized localized bulk notification payload.
		 */
		public LocalizedBulkPush build() {
			return new LocalizedBulkPushObject(this.notification, this.userNotifications);
		}

		@Getter
		@RequiredArgsConstructor
		private static final class LocalizedBulkPushObject implements LocalizedBulkPush {
			private final LocalizedPushPayload notification;
			private final Collection<LocalizedUserNotification> userNotifications;
		}
	}
}

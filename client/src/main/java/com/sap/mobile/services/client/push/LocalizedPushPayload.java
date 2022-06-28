package com.sap.mobile.services.client.push;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * Root payload element for push service localized
 * push, @see <a target="_top"
 * href=
 * "https://help.sap.com/doc/f53c64b93e5140918d676b927a3cd65b/Cloud/en-US/docs-en/guides/features/push/api/localization.html#push-service-based-localization">Push
 * Service Based Localization</a>. Use the {@link Builder} to define and build
 * the element.
 */
public interface LocalizedPushPayload {

	static Builder builder() {
		return new Builder();
	}

	PushPayload getNotification();

	Map<String, ? extends PushPayload> getNotifications();

	/**
	 * Helper for server localized push payload creation.
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	final class Builder {

		private PushPayload notification;
		private Map<String, ? extends PushPayload> notifications;

		/**
		 * Sets the fallback notification sent when the device registration has no
		 * locale or the localized notification map has no matching locale for the
		 * device locale, @see <a href=
		 * "https://help.sap.com/doc/f53c64b93e5140918d676b927a3cd65b/Cloud/en-US/docs-en/guides/features/push/api/localization.html#language-fallback">Localized
		 * Push - Language Fallback</a>
		 * 
		 * @param notification the default notification.
		 * @return a builder with the default notification.
		 */
		public Builder notification(PushPayload notification) {
			return new Builder(notification, this.notifications);
		}

		/**
		 * Sets/replaces the map of localized push notifications.
		 * 
		 * @param notifications a map of localized notifications.
		 * @return a builder with the map of localized notifications.
		 */
		public Builder notifications(Map<String, ? extends PushPayload> notifications) {
			return new Builder(this.notification, notifications);
		}

		/**
		 * Add a single localized notification to the existing localized notification
		 * map.
		 * 
		 * @param locale      the locale
		 * @param pushPayload the localized notification
		 * @return a builder with the updated map of localized notifications.
		 */
		public Builder addLocale(Locale locale, PushPayload pushPayload) {
			return this.addLocale(locale.toString(), pushPayload);
		}

		/**
		 * Add a single localized notification to the existing localized notification
		 * map.
		 * 
		 * @param locale      the locale
		 * @param pushPayload the localized notification
		 * @return a builder with the updated map of localized notifications.
		 */
		public Builder addLocale(String locale, PushPayload pushPayload) {
			Map<String, PushPayload> notifications = new HashMap<>(this.notifications);
			notifications.put(locale, pushPayload);
			return new Builder(this.notification, notifications);
		}

		/**
		 * @return the server localized localized bulk notification payload.
		 */
		public LocalizedPushPayload build() {
			return new LocalizedPushPayloadObject(this.notification, this.notifications);
		}

		@Getter
		@RequiredArgsConstructor
		private static class LocalizedPushPayloadObject implements LocalizedPushPayload {
			private final PushPayload notification;
			private final Map<String, ? extends PushPayload> notifications;
		}
	}
}

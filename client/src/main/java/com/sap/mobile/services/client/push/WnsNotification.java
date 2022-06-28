package com.sap.mobile.services.client.push;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * WNS specific notification request element. Use {@link Builder} to configure
 * and build the request payload.
 */
public interface WnsNotification {

	static Builder builder() {
		return new Builder();
	}

	List<String> getSchema();

	String getBadge();

	WnsNotificationCommands getCommands();

	WnsNotificationAudio getAudio();

	List<WnsNotificationImage> getImage();

	String getVersion();

	String getLang();

	String getBaseUri();

	String getTileTemplate();

	String getToastTemplate();

	String getRawData();

	List<String> getMessage();

	/**
	 * WNS specific notification properties.
	 * 
	 * @see <a target="_top" href=
	 *      "https://docs.microsoft.com/en-us/windows/apps/design/shell/tiles-and-notifications/windows-push-notification-services--wns--overview">https://docs.microsoft.com/en-us/windows/apps/design/shell/tiles-and-notifications/windows-push-notification-services--wns--overview</a>
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	final class Builder {
		private List<String> schema;
		private String badge;
		private WnsNotificationCommands commands;
		private WnsNotificationAudio audio;
		private List<WnsNotificationImage> image;
		private String version;
		private String lang;
		private String baseUri;
		private String tileTemplate;
		private String toastTemplate;
		private String rawData;
		private List<String> message;

		/**
		 * The schema indicates the type of notification and how WNS should handle it.
		 **/
		public Builder schema(List<String> schema) {
			return new Builder(schema, this.badge, this.commands, this.audio, this.image, this.version, this.lang,
					this.baseUri, this.tileTemplate, this.toastTemplate, this.rawData, this.message);
		}

		/**
		 * Badge is used to provide status or summary info in the form of a
		 * system-provided glyph or a number from 1-99.
		 * Badges also appear on the task bar icon for an app.
		 */
		public Builder badge(String badge) {
			return new Builder(this.schema, badge, this.commands, this.audio, this.image, this.version, this.lang,
					this.baseUri, this.tileTemplate, this.toastTemplate, this.rawData, this.message);
		}

		/**
		 * Specifies that the notification is being used to indicate an incoming call or
		 * an alarm, with appropriate
		 * commands associated with each scenario.
		 */

		public Builder commands(WnsNotificationCommands commands) {
			return new Builder(this.schema, this.badge, commands, this.audio, this.image, this.version, this.lang,
					this.baseUri, this.tileTemplate, this.toastTemplate, this.rawData, this.message);
		}

		/**
		 * Specifies a sound to play when a notification is displayed.
		 * This element also allows you to mute any notification audio.
		 */
		public Builder audio(WnsNotificationAudio audio) {
			return new Builder(this.schema, this.badge, this.commands, audio, this.image, this.version, this.lang,
					this.baseUri, this.tileTemplate, this.toastTemplate, this.rawData, this.message);
		}

		/**
		 * Specifies an image used in the template.
		 */
		public Builder image(List<WnsNotificationImage> image) {
			return new Builder(this.schema, this.badge, this.commands, this.audio, image, this.version, this.lang,
					this.baseUri, this.tileTemplate, this.toastTemplate, this.rawData, this.message);
		}

		/**
		 * The version element sets the version property at the notification requests
		 */
		public Builder version(String version) {
			return new Builder(this.schema, this.badge, this.commands, this.audio, this.image, version, this.lang,
					this.baseUri, this.tileTemplate, this.toastTemplate, this.rawData, this.message);
		}

		/**
		 * The target locale of the XML payload
		 * 
		 * @see <a target="_top" href=
		 *      "https://docs.microsoft.com/en-us/uwp/schemas/tiles/toastschema/element-binding">https://docs.microsoft.com/en-us/uwp/schemas/tiles/toastschema/element-binding</a>
		 */
		public Builder lang(String lang) {
			return new Builder(this.schema, this.badge, this.commands, this.audio, this.image, this.version, lang,
					this.baseUri, this.tileTemplate, this.toastTemplate, this.rawData, this.message);
		}

		/**
		 * A default base URI that is combined with relative URIs in image source
		 * attributes.
		 * 
		 * @see <a target="_top" href=
		 *      "https://docs.microsoft.com/en-us/uwp/schemas/tiles/toastschema/element-binding">https://docs.microsoft.com/en-us/uwp/schemas/tiles/toastschema/element-binding</a>
		 */
		public Builder baseUri(String baseUri) {
			return new Builder(this.schema, this.badge, this.commands, this.audio, this.image, this.version, this.lang,
					baseUri, this.tileTemplate, this.toastTemplate, this.rawData, this.message);
		}

		/**
		 * Specifies the content template to use in a tile update.
		 * 
		 * @see <a target="_top" href=
		 *      "https://docs.microsoft.com/en-us/uwp/api/windows.ui.notifications.tiletemplatetype">https://docs.microsoft.com/en-us/uwp/api/windows.ui.notifications.tiletemplatetype</a>
		 */
		public Builder tileTemplate(String tileTemplate) {
			return new Builder(this.schema, this.badge, this.commands, this.audio, this.image, this.version, this.lang,
					this.baseUri, tileTemplate, this.toastTemplate, this.rawData, this.message);
		}

		/**
		 * Specifies the template to use in a toast notification.
		 * 
		 * @see <a target="_top" href=
		 *      "https://docs.microsoft.com/en-us/uwp/api/Windows.UI.Notifications.ToastTemplateType">https://docs.microsoft.com/en-us/uwp/api/Windows.UI.Notifications.ToastTemplateType</a>
		 */
		public Builder toastTemplate(String toastTemplate) {
			return new Builder(this.schema, this.badge, this.commands, this.audio, this.image, this.version, this.lang,
					this.baseUri, this.tileTemplate, toastTemplate, this.rawData, this.message);
		}

		/**
		 * The rawData element represents a certain WNS push schema (wns/raw).
		 * 
		 * @see <a target="_top" href=
		 *      "https://docs.microsoft.com/en-us/windows/apps/design/shell/tiles-and-notifications/raw-notification-overview">https://docs.microsoft.com/en-us/windows/apps/design/shell/tiles-and-notifications/raw-notification-overview</a>
		 */
		public Builder rawData(String rawData) {
			return new Builder(this.schema, this.badge, this.commands, this.audio, this.image, this.version, this.lang,
					this.baseUri, this.tileTemplate, this.toastTemplate, rawData, this.message);
		}

		/**
		 * The message element defines the text attribute for tile and toast
		 * notifications.
		 */
		public Builder message(List<String> message) {
			return new Builder(this.schema, this.badge, this.commands, this.audio, this.image, this.version, this.lang,
					this.baseUri, this.tileTemplate, this.toastTemplate, this.rawData, message);
		}

		public WnsNotification build() {
			return new WnsNotificationObject(this.schema, this.badge, this.commands, this.audio, this.image,
					this.version, this.lang, this.baseUri, this.tileTemplate, this.toastTemplate, this.rawData,
					this.message);
		}

		@Getter
		@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
		private static class WnsNotificationObject implements WnsNotification {
			private final List<String> schema;
			private final String badge;
			private final WnsNotificationCommands commands;
			private final WnsNotificationAudio audio;
			private final List<WnsNotificationImage> image;
			private final String version;
			private final String lang;
			private final String baseUri;
			private final String tileTemplate;
			private final String toastTemplate;
			private final String rawData;
			private final List<String> message;
		}
	}
}

package com.sap.mobile.services.client.push;

import java.util.Date;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

public interface ApnsNotification {

	static Builder builder() {
		return new Builder();
	}

	Date getExpiration();

	String getCategory();

	Boolean getContentAvailable();

	PushType getPushType();

	String getCustomValues();

	String getSound();

	ApnsCustomSound getCustomSound();

	String getTopic();

	String getAlertBody();

	String getLocalizedAlertKey();

	List<String> getLocalizedAlertArguments();

	String getAlertTitle();

	String getLocalizedAlertTitleKey();

	List<String> getLocalizedAlertTitleArguments();

	String getAlertSubtitle();

	String getLocalizedAlertSubtitleKey();

	List<String> getLocalizedAlertSubtitleArguments();

	String getLaunchImageFileName();

	Boolean getShowActionButton();

	String getActionButtonLabel();

	String getLocalizedActionButtonKey();

	List<String> getUrlArguments();

	String getThreadId();

	Boolean getMutableContent();

	/**
	 * APNS specific notification properties. See
	 * 
	 * @see <a href=
	 *      "https://developer.apple.com/documentation/usernotifications/setting_up_a_remote_notification_server/generating_a_remote_notification">
	 *      Generating a remote notification</a>.
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	final class Builder {
		private Date expiration;
		private String category;
		private Boolean contentAvailable;
		private PushType pushType;
		private String customValues;
		private String sound;
		private ApnsCustomSound customSound;
		private String topic;
		private String alertBody;
		private String localizedAlertKey;
		private List<String> localizedAlertArguments;
		private String alertTitle;
		private String localizedAlertTitleKey;
		private List<String> localizedAlertTitleArguments;
		private String alertSubtitle;
		private String localizedAlertSubtitleKey;
		private List<String> localizedAlertSubtitleArguments;
		private String launchImageFileName;
		private Boolean showActionButton;
		private String actionButtonLabel;
		private String localizedActionButtonKey;
		private List<String> urlArguments;
		private String threadId;
		private Boolean mutableContent;

		/**
		 * expire notification at given timestamp
		 */
		public Builder expiration(Date expiration) {
			return new Builder(expiration, this.category, this.contentAvailable, this.pushType, this.customValues,
					this.sound, this.customSound, this.topic, this.alertBody, this.localizedAlertKey,
					this.localizedAlertArguments, this.alertTitle, this.localizedAlertTitleKey,
					this.localizedAlertTitleArguments, this.alertSubtitle, this.localizedAlertSubtitleKey,
					this.localizedAlertSubtitleArguments, this.launchImageFileName, this.showActionButton,
					this.actionButtonLabel, this.localizedActionButtonKey, this.urlArguments, this.threadId,
					this.mutableContent);
		}

		/**
		 * Mobile apps can register actions during launch grouped as category. This
		 * value allows actions to be executed directly by the notification in the
		 * notification center.
		 */
		public Builder category(String category) {
			return new Builder(this.expiration, category, this.contentAvailable, this.pushType, this.customValues,
					this.sound, this.customSound, this.topic, this.alertBody, this.localizedAlertKey,
					this.localizedAlertArguments, this.alertTitle, this.localizedAlertTitleKey,
					this.localizedAlertTitleArguments, this.alertSubtitle, this.localizedAlertSubtitleKey,
					this.localizedAlertSubtitleArguments, this.launchImageFileName, this.showActionButton,
					this.actionButtonLabel, this.localizedActionButtonKey, this.urlArguments, this.threadId,
					this.mutableContent);
		}

		/**
		 * Content available marks background notification without visual text.
		 */
		public Builder contentAvailable(Boolean contentAvailable) {
			return new Builder(this.expiration, this.category, contentAvailable, this.pushType, this.customValues,
					this.sound, this.customSound, this.topic, this.alertBody, this.localizedAlertKey,
					this.localizedAlertArguments, this.alertTitle, this.localizedAlertTitleKey,
					this.localizedAlertTitleArguments, this.alertSubtitle, this.localizedAlertSubtitleKey,
					this.localizedAlertSubtitleArguments, this.launchImageFileName, this.showActionButton,
					this.actionButtonLabel, this.localizedActionButtonKey, this.urlArguments, this.threadId,
					this.mutableContent);
		}

		/**
		 * Push Type, see
		 * 
		 * @see <a href=
		 *      "https://developer.apple.com/documentation/usernotifications/setting_up_a_remote_notification_server/sending_notification_requests_to_apns">
		 *      Sending notification requests</a>
		 */
		public Builder pushType(PushType pushType) {
			return new Builder(this.expiration, this.category, this.contentAvailable, pushType, this.customValues,
					this.sound, this.customSound, this.topic, this.alertBody, this.localizedAlertKey,
					this.localizedAlertArguments, this.alertTitle, this.localizedAlertTitleKey,
					this.localizedAlertTitleArguments, this.alertSubtitle, this.localizedAlertSubtitleKey,
					this.localizedAlertSubtitleArguments, this.launchImageFileName, this.showActionButton,
					this.actionButtonLabel, this.localizedActionButtonKey, this.urlArguments, this.threadId,
					this.mutableContent);
		}

		/**
		 * Custom values for APNS only
		 */
		public Builder customValues(String customValues) {
			return new Builder(this.expiration, this.category, this.contentAvailable, this.pushType, customValues,
					this.sound, this.customSound, this.topic, this.alertBody, this.localizedAlertKey,
					this.localizedAlertArguments, this.alertTitle, this.localizedAlertTitleKey,
					this.localizedAlertTitleArguments, this.alertSubtitle, this.localizedAlertSubtitleKey,
					this.localizedAlertSubtitleArguments, this.launchImageFileName, this.showActionButton,
					this.actionButtonLabel, this.localizedActionButtonKey, this.urlArguments, this.threadId,
					this.mutableContent);
		}

		/**
		 * Reference to a sound resource in the app. default will play the system sound
		 * 
		 * @deprecated please use {@link Builder#customSound}
		 */
		public Builder sound(String sound) {
			return new Builder(this.expiration, this.category, this.contentAvailable, this.pushType, this.customValues,
					sound, this.customSound, this.topic, this.alertBody, this.localizedAlertKey,
					this.localizedAlertArguments, this.alertTitle, this.localizedAlertTitleKey,
					this.localizedAlertTitleArguments, this.alertSubtitle, this.localizedAlertSubtitleKey,
					this.localizedAlertSubtitleArguments, this.launchImageFileName, this.showActionButton,
					this.actionButtonLabel, this.localizedActionButtonKey, this.urlArguments, this.threadId,
					this.mutableContent);
		}

		/**
		 * Detailed APNS sound definition. Use this when sending critical
		 * notifications.
		 */
		public Builder customSound(ApnsCustomSound customSound) {
			return new Builder(this.expiration, this.category, this.contentAvailable, this.pushType, this.customValues,
					this.sound, customSound, this.topic, this.alertBody, this.localizedAlertKey,
					this.localizedAlertArguments, this.alertTitle, this.localizedAlertTitleKey,
					this.localizedAlertTitleArguments, this.alertSubtitle, this.localizedAlertSubtitleKey,
					this.localizedAlertSubtitleArguments, this.launchImageFileName, this.showActionButton,
					this.actionButtonLabel, this.localizedActionButtonKey, this.urlArguments, this.threadId,
					this.mutableContent);
		}

		/**
		 * The topic for the notification. In general, the topic is your app’s bundle
		 * ID/app ID. It can have a suffix based on the type of push notification.
		 */
		public Builder topic(String topic) {
			return new Builder(this.expiration, this.category, this.contentAvailable, this.pushType, this.customValues,
					this.sound, this.customSound, topic, this.alertBody, this.localizedAlertKey,
					this.localizedAlertArguments, this.alertTitle, this.localizedAlertTitleKey,
					this.localizedAlertTitleArguments, this.alertSubtitle, this.localizedAlertSubtitleKey,
					this.localizedAlertSubtitleArguments, this.launchImageFileName, this.showActionButton,
					this.actionButtonLabel, this.localizedActionButtonKey, this.urlArguments, this.threadId,
					this.mutableContent);
		}

		/**
		 * The notification body. {@link Builder#localizedAlertKey}
		 * should
		 * be used to retrieve the subtitle from the localized resources of the app
		 * based on the user's language configuration when sending templated messages.
		 */
		public Builder alertBody(String alertBody) {
			return new Builder(this.expiration, this.category, this.contentAvailable, this.pushType, this.customValues,
					this.sound, this.customSound, this.topic, alertBody, this.localizedAlertKey,
					this.localizedAlertArguments, this.alertTitle, this.localizedAlertTitleKey,
					this.localizedAlertTitleArguments, this.alertSubtitle, this.localizedAlertSubtitleKey,
					this.localizedAlertSubtitleArguments, this.launchImageFileName, this.showActionButton,
					this.actionButtonLabel, this.localizedActionButtonKey, this.urlArguments, this.threadId,
					this.mutableContent);
		}

		/**
		 * Should be used to retrieve the title from the localized resources of the app
		 * based on the user's language configuration.
		 */
		public Builder localizedAlertKey(String localizedAlertKey) {
			return new Builder(this.expiration, this.category, this.contentAvailable, this.pushType, this.customValues,
					this.sound, this.customSound, this.topic, this.alertBody, localizedAlertKey,
					this.localizedAlertArguments, this.alertTitle, this.localizedAlertTitleKey,
					this.localizedAlertTitleArguments, this.alertSubtitle, this.localizedAlertSubtitleKey,
					this.localizedAlertSubtitleArguments, this.launchImageFileName, this.showActionButton,
					this.actionButtonLabel, this.localizedActionButtonKey, this.urlArguments, this.threadId,
					this.mutableContent);
		}

		/**
		 * Localization arguments for the body
		 */
		public Builder localizedAlertArguments(List<String> localizedAlertArguments) {
			return new Builder(this.expiration, this.category, this.contentAvailable, this.pushType, this.customValues,
					this.sound, this.customSound, this.topic, this.alertBody, this.localizedAlertKey,
					localizedAlertArguments, this.alertTitle, this.localizedAlertTitleKey,
					this.localizedAlertTitleArguments, this.alertSubtitle, this.localizedAlertSubtitleKey,
					this.localizedAlertSubtitleArguments, this.launchImageFileName, this.showActionButton,
					this.actionButtonLabel, this.localizedActionButtonKey, this.urlArguments, this.threadId,
					this.mutableContent);
		}

		/**
		 * The notification title.
		 * {@link Builder#localizedAlertTitleKey} should
		 * be used to retrieve the subtitle from the localized resources of the app
		 * based on the user's language configuration when sending templated messages.
		 */
		public Builder alertTitle(String alertTitle) {
			return new Builder(this.expiration, this.category, this.contentAvailable, this.pushType, this.customValues,
					this.sound, this.customSound, this.topic, this.alertBody, this.localizedAlertKey,
					this.localizedAlertArguments, alertTitle, this.localizedAlertTitleKey,
					this.localizedAlertTitleArguments, this.alertSubtitle, this.localizedAlertSubtitleKey,
					this.localizedAlertSubtitleArguments, this.launchImageFileName, this.showActionButton,
					this.actionButtonLabel, this.localizedActionButtonKey, this.urlArguments, this.threadId,
					this.mutableContent);
		}

		/**
		 * Should be used to retrieve the title from the localized resources of the app
		 * based on the user's language configuration.
		 */
		public Builder localizedAlertTitleKey(String localizedAlertTitleKey) {
			return new Builder(this.expiration, this.category, this.contentAvailable, this.pushType, this.customValues,
					this.sound, this.customSound, this.topic, this.alertBody, this.localizedAlertKey,
					this.localizedAlertArguments, this.alertTitle, localizedAlertTitleKey,
					this.localizedAlertTitleArguments, this.alertSubtitle, this.localizedAlertSubtitleKey,
					this.localizedAlertSubtitleArguments, this.launchImageFileName, this.showActionButton,
					this.actionButtonLabel, this.localizedActionButtonKey, this.urlArguments, this.threadId,
					this.mutableContent);
		}

		/**
		 * Localization arguments for the title
		 */
		public Builder localizedAlertTitleArguments(List<String> localizedAlertTitleArguments) {
			return new Builder(this.expiration, this.category, this.contentAvailable, this.pushType, this.customValues,
					this.sound, this.customSound, this.topic, this.alertBody, this.localizedAlertKey,
					this.localizedAlertArguments, this.alertTitle, this.localizedAlertTitleKey,
					localizedAlertTitleArguments, this.alertSubtitle, this.localizedAlertSubtitleKey,
					this.localizedAlertSubtitleArguments, this.launchImageFileName, this.showActionButton,
					this.actionButtonLabel, this.localizedActionButtonKey, this.urlArguments, this.threadId,
					this.mutableContent);
		}

		/**
		 * The notification subtitle.
		 * {@link Builder#localizedAlertSubtitleKey} should
		 * be used to retrieve the subtitle from the localized resources of the app
		 * based on the user's language configuration when sending templated messages.
		 */
		public Builder alertSubtitle(String alertSubtitle) {
			return new Builder(this.expiration, this.category, this.contentAvailable, this.pushType, this.customValues,
					this.sound, this.customSound, this.topic, this.alertBody, this.localizedAlertKey,
					this.localizedAlertArguments, this.alertTitle, this.localizedAlertTitleKey,
					this.localizedAlertTitleArguments, alertSubtitle, this.localizedAlertSubtitleKey,
					this.localizedAlertSubtitleArguments, this.launchImageFileName, this.showActionButton,
					this.actionButtonLabel, this.localizedActionButtonKey, this.urlArguments, this.threadId,
					this.mutableContent);
		}

		/**
		 * Should be used to retrieve the subtitle from the localized resources of the
		 * app
		 * based on the user's language configuration.
		 */
		public Builder localizedAlertSubtitleKey(String localizedAlertSubtitleKey) {
			return new Builder(this.expiration, this.category, this.contentAvailable, this.pushType, this.customValues,
					this.sound, this.customSound, this.topic, this.alertBody, this.localizedAlertKey,
					this.localizedAlertArguments, this.alertTitle, this.localizedAlertTitleKey,
					this.localizedAlertTitleArguments, this.alertSubtitle, localizedAlertSubtitleKey,
					this.localizedAlertSubtitleArguments, this.launchImageFileName, this.showActionButton,
					this.actionButtonLabel, this.localizedActionButtonKey, this.urlArguments, this.threadId,
					this.mutableContent);
		}

		/**
		 * Localization arguments for the subtitle
		 */
		public Builder localizedAlertSubtitleArguments(List<String> localizedAlertSubtitleArguments) {
			return new Builder(this.expiration, this.category, this.contentAvailable, this.pushType, this.customValues,
					this.sound, this.customSound, this.topic, this.alertBody, this.localizedAlertKey,
					this.localizedAlertArguments, this.alertTitle, this.localizedAlertTitleKey,
					this.localizedAlertTitleArguments, this.alertSubtitle, this.localizedAlertSubtitleKey,
					localizedAlertSubtitleArguments, this.launchImageFileName, this.showActionButton,
					this.actionButtonLabel, this.localizedActionButtonKey, this.urlArguments, this.threadId,
					this.mutableContent);
		}

		/**
		 * The name of the image or storyboard to use when your app launches because of
		 * the notification.
		 */
		public Builder launchImageFileName(String launchImageFileName) {
			return new Builder(this.expiration, this.category, this.contentAvailable, this.pushType, this.customValues,
					this.sound, this.customSound, this.topic, this.alertBody, this.localizedAlertKey,
					this.localizedAlertArguments, this.alertTitle, this.localizedAlertTitleKey,
					this.localizedAlertTitleArguments, this.alertSubtitle, this.localizedAlertSubtitleKey,
					this.localizedAlertSubtitleArguments, launchImageFileName, this.showActionButton,
					this.actionButtonLabel, this.localizedActionButtonKey, this.urlArguments, this.threadId,
					this.mutableContent);
		}

		/**
		 * Safari only: Show notification action button
		 */
		public Builder showActionButton(Boolean showActionButton) {
			return new Builder(this.expiration, this.category, this.contentAvailable, this.pushType, this.customValues,
					this.sound, this.customSound, this.topic, this.alertBody, this.localizedAlertKey,
					this.localizedAlertArguments, this.alertTitle, this.localizedAlertTitleKey,
					this.localizedAlertTitleArguments, this.alertSubtitle, this.localizedAlertSubtitleKey,
					this.localizedAlertSubtitleArguments, this.launchImageFileName, showActionButton,
					this.actionButtonLabel, this.localizedActionButtonKey, this.urlArguments, this.threadId,
					this.mutableContent);
		}

		/**
		 * Safari only: The literal text of the action button to be shown for the push
		 * notification.
		 */
		public Builder actionButtonLabel(String actionButtonLabel) {
			return new Builder(this.expiration, this.category, this.contentAvailable, this.pushType, this.customValues,
					this.sound, this.customSound, this.topic, this.alertBody, this.localizedAlertKey,
					this.localizedAlertArguments, this.alertTitle, this.localizedAlertTitleKey,
					this.localizedAlertTitleArguments, this.alertSubtitle, this.localizedAlertSubtitleKey,
					this.localizedAlertSubtitleArguments, this.launchImageFileName, this.showActionButton,
					actionButtonLabel, this.localizedActionButtonKey, this.urlArguments, this.threadId,
					this.mutableContent);
		}

		/**
		 * Safari only: Localized key of the action button to be shown for the push
		 * notification.
		 */
		public Builder localizedActionButtonKey(String localizedActionButtonKey) {
			return new Builder(this.expiration, this.category, this.contentAvailable, this.pushType, this.customValues,
					this.sound, this.customSound, this.topic, this.alertBody, this.localizedAlertKey,
					this.localizedAlertArguments, this.alertTitle, this.localizedAlertTitleKey,
					this.localizedAlertTitleArguments, this.alertSubtitle, this.localizedAlertSubtitleKey,
					this.localizedAlertSubtitleArguments, this.launchImageFileName, this.showActionButton,
					this.actionButtonLabel, localizedActionButtonKey, this.urlArguments, this.threadId,
					this.mutableContent);
		}

		/**
		 * List of arguments to populate placeholders in the urlFormatString associated
		 * with a Safari push notification.
		 */
		public Builder urlArguments(List<String> urlArguments) {
			return new Builder(this.expiration, this.category, this.contentAvailable, this.pushType, this.customValues,
					this.sound, this.customSound, this.topic, this.alertBody, this.localizedAlertKey,
					this.localizedAlertArguments, this.alertTitle, this.localizedAlertTitleKey,
					this.localizedAlertTitleArguments, this.alertSubtitle, this.localizedAlertSubtitleKey,
					this.localizedAlertSubtitleArguments, this.launchImageFileName, this.showActionButton,
					this.actionButtonLabel, this.localizedActionButtonKey, urlArguments, this.threadId,
					this.mutableContent);
		}

		/**
		 * Thread-Ids allows grouping of notification on application level
		 */
		public Builder threadId(String threadId) {
			return new Builder(this.expiration, this.category, this.contentAvailable, this.pushType, this.customValues,
					this.sound, this.customSound, this.topic, this.alertBody, this.localizedAlertKey,
					this.localizedAlertArguments, this.alertTitle, this.localizedAlertTitleKey,
					this.localizedAlertTitleArguments, this.alertSubtitle, this.localizedAlertSubtitleKey,
					this.localizedAlertSubtitleArguments, this.launchImageFileName, this.showActionButton,
					this.actionButtonLabel, this.localizedActionButtonKey, this.urlArguments, threadId,
					this.mutableContent);
		}

		/**
		 * Notifications with mutable content set and no visual alert is defined are
		 * processed by the mobile app, prior displayed in the notification center
		 */
		public Builder mutableContent(Boolean mutableContent) {
			return new Builder(this.expiration, this.category, this.contentAvailable, this.pushType, this.customValues,
					this.sound, this.customSound, this.topic, this.alertBody, this.localizedAlertKey,
					this.localizedAlertArguments, this.alertTitle, this.localizedAlertTitleKey,
					this.localizedAlertTitleArguments, this.alertSubtitle, this.localizedAlertSubtitleKey,
					this.localizedAlertSubtitleArguments, this.launchImageFileName, this.showActionButton,
					this.actionButtonLabel, this.localizedActionButtonKey, this.urlArguments, this.threadId,
					mutableContent);
		}

		public ApnsNotification build() {
			return new ApnsNotificationObject(this.expiration, this.category, this.contentAvailable, this.pushType,
					this.customValues, this.sound, this.customSound, this.topic, this.alertBody,
					this.localizedAlertKey,
					this.localizedAlertArguments, this.alertTitle, this.localizedAlertTitleKey,
					this.localizedAlertTitleArguments, this.alertSubtitle, this.localizedAlertSubtitleKey,
					this.localizedAlertSubtitleArguments, this.launchImageFileName, this.showActionButton,
					this.actionButtonLabel, this.localizedActionButtonKey, this.urlArguments, this.threadId,
					this.mutableContent);
		}

		@Getter
		@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
		private static class ApnsNotificationObject implements ApnsNotification {
			private final Date expiration;
			private final String category;
			private final Boolean contentAvailable;
			private final PushType pushType;
			private final String customValues;
			private final String sound;
			private final ApnsCustomSound customSound;
			private final String topic;
			private final String alertBody;
			private final String localizedAlertKey;
			private final List<String> localizedAlertArguments;
			private final String alertTitle;
			private final String localizedAlertTitleKey;
			private final List<String> localizedAlertTitleArguments;
			private final String alertSubtitle;
			private final String localizedAlertSubtitleKey;
			private final List<String> localizedAlertSubtitleArguments;
			private final String launchImageFileName;
			private final Boolean showActionButton;
			private final String actionButtonLabel;
			private final String localizedActionButtonKey;
			private final List<String> urlArguments;
			private final String threadId;
			private final Boolean mutableContent;
		}
	}
}

package com.sap.mobile.services.client.push;

import java.time.Duration;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

public interface GcmNotification {

	static Builder builder() {
		return new Builder();
	}

	String getCollapseKey();

	Boolean getDelayWhileIdle();

	Duration getTimeToLive();

	String getRestrictedPackageName();

	String getTitle();

	String getBody();

	String getIcon();

	String getSound();

	String getTag();

	String getColor();

	String getClickAction();

	String getBodyLocKey();

	String getBodyLocArgs();

	List<String> getBodyLocArgsArray();

	String getTitleLocKey();

	String getTitleLocArgs();

	List<String> getTitleLocArgsArray();

	/**
	 * Firebase/Android specific notification parameter. See @see
	 * <a target="_top" href=
	 * "https://firebase.google.com/docs/cloud-messaging/concept-options?hl=en#notifications_and_data_messages">Notifications
	 * and data messages</a>
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	final class Builder {
		private String collapseKey;
		private Boolean delayWhileIdle;
		private Duration timeToLive;
		private String restrictedPackageName;
		private String title;
		private String body;
		private String icon;
		private String sound;
		private String tag;
		private String color;
		private String clickAction;
		private String bodyLocKey;
		private String bodyLocArgs;
		private List<String> bodyLocArgsArray;
		private String titleLocKey;
		private String titleLocArgs;
		private List<String> titleLocArgsArray;

		/**
		 * Collapsible messages replace old messages with the same key with new messages
		 * with the same key. Android can store up to 100 messages without collapsing.
		 * 
		 * @see <a target="_top" href=
		 *      "https://firebase.google.com/docs/cloud-messaging/concept-options#collapsible_and_non-collapsible_messages">Collapsible
		 *      and non-collapsible messages</a>
		 */
		public Builder collapseKey(String collapseKey) {
			return new Builder(collapseKey, this.delayWhileIdle, this.timeToLive, this.restrictedPackageName,
					this.title, this.body, this.icon, this.sound, this.tag, this.color, this.clickAction,
					this.bodyLocKey, this.bodyLocArgs, this.bodyLocArgsArray, this.titleLocKey, this.titleLocArgs,
					this.titleLocArgsArray);
		}

		/**
		 * Message delivery by FCM is delayed when the device is idle.
		 * 
		 * @deprecated use {@link Builder#timeToLive } instead
		 */
		public Builder delayWhileIdle(Boolean delayWhileIdle) {
			return new Builder(this.collapseKey, delayWhileIdle, this.timeToLive, this.restrictedPackageName,
					this.title, this.body, this.icon, this.sound, this.tag, this.color, this.clickAction,
					this.bodyLocKey, this.bodyLocArgs, this.bodyLocArgsArray, this.titleLocKey, this.titleLocArgs,
					this.titleLocArgsArray);
		}

		/**
		 * When an app server posts a message to FCM and receives a message ID back, it
		 * does not mean that the message was already delivered to the device. Rather,
		 * it means that it was accepted for delivery. What happens to the message after
		 * it is accepted depends on many factors. This property can be used to deliver
		 * the message only in the next duration
		 */
		public Builder timeToLive(Duration timeToLive) {
			return new Builder(this.collapseKey, this.delayWhileIdle, timeToLive, this.restrictedPackageName,
					this.title, this.body, this.icon, this.sound, this.tag, this.color, this.clickAction,
					this.bodyLocKey, this.bodyLocArgs, this.bodyLocArgsArray, this.titleLocKey, this.titleLocArgs,
					this.titleLocArgsArray);
		}

		/**
		 * Android allows you to use one FCM service instance for multiple apps with
		 * different package names. This property will restrict the message to apps with
		 * the given package name.
		 */
		public Builder restrictedPackageName(String restrictedPackageName) {
			return new Builder(this.collapseKey, this.delayWhileIdle, this.timeToLive, restrictedPackageName,
					this.title, this.body, this.icon, this.sound, this.tag, this.color, this.clickAction,
					this.bodyLocKey, this.bodyLocArgs, this.bodyLocArgsArray, this.titleLocKey, this.titleLocArgs,
					this.titleLocArgsArray);
		}

		/**
		 * If set, this field takes priority over the alert field of the notification
		 * element {@link Builder#titleLocKey} should be used to retrieve
		 * the title from the localized resources of the app based on the user's
		 * language configuration.
		 */
		public Builder title(String title) {
			return new Builder(this.collapseKey, this.delayWhileIdle, this.timeToLive, this.restrictedPackageName,
					title, this.body, this.icon, this.sound, this.tag, this.color, this.clickAction, this.bodyLocKey,
					this.bodyLocArgs, this.bodyLocArgsArray, this.titleLocKey, this.titleLocArgs,
					this.titleLocArgsArray);
		}

		/**
		 * If set, this field takes priority over the alert field of the notification
		 * element {@link Builder#bodyLocKey} should be used to retrieve
		 * the title from the localized resources of the app based on the user's
		 * language configuration.
		 */
		public Builder body(String body) {
			return new Builder(this.collapseKey, this.delayWhileIdle, this.timeToLive, this.restrictedPackageName,
					this.title, body, this.icon, this.sound, this.tag, this.color, this.clickAction, this.bodyLocKey,
					this.bodyLocArgs, this.bodyLocArgsArray, this.titleLocKey, this.titleLocArgs,
					this.titleLocArgsArray);
		}

		/**
		 * Allows you to implement a custom app icon for the message instead of the
		 * default. The icon resource must be present in the app resources.
		 */
		public Builder icon(String icon) {
			return new Builder(this.collapseKey, this.delayWhileIdle, this.timeToLive, this.restrictedPackageName,
					this.title, this.body, icon, this.sound, this.tag, this.color, this.clickAction, this.bodyLocKey,
					this.bodyLocArgs, this.bodyLocArgsArray, this.titleLocKey, this.titleLocArgs,
					this.titleLocArgsArray);
		}

		/**
		 * Allows you to play a custom sound when the message is received. If set, this
		 * field takes priority over the sound field of the notification element. The
		 * custom sound resource must be present in the app resource folder
		 * {@code res\raw}. {@code default} selects the default notification sound.
		 */
		public Builder sound(String sound) {
			return new Builder(this.collapseKey, this.delayWhileIdle, this.timeToLive, this.restrictedPackageName,
					this.title, this.body, this.icon, sound, this.tag, this.color, this.clickAction, this.bodyLocKey,
					this.bodyLocArgs, this.bodyLocArgsArray, this.titleLocKey, this.titleLocArgs,
					this.titleLocArgsArray);
		}

		/**
		 * Notification with a tag replaces older notifications with the same tag on the
		 * device.
		 */
		public Builder tag(String tag) {
			return new Builder(this.collapseKey, this.delayWhileIdle, this.timeToLive, this.restrictedPackageName,
					this.title, this.body, this.icon, this.sound, tag, this.color, this.clickAction, this.bodyLocKey,
					this.bodyLocArgs, this.bodyLocArgsArray, this.titleLocKey, this.titleLocArgs,
					this.titleLocArgsArray);
		}

		/**
		 * The notification's icon color, expressed in #rrggbb format.
		 */
		public Builder color(String color) {
			return new Builder(this.collapseKey, this.delayWhileIdle, this.timeToLive, this.restrictedPackageName,
					this.title, this.body, this.icon, this.sound, this.tag, color, this.clickAction, this.bodyLocKey,
					this.bodyLocArgs, this.bodyLocArgsArray, this.titleLocKey, this.titleLocArgs,
					this.titleLocArgsArray);
		}

		/**
		 * The action associated with a user click on the notification. If specified, an
		 * activity with a matching intent filter is launched when a user clicks on the
		 * notification.
		 */
		public Builder clickAction(String clickAction) {
			return new Builder(this.collapseKey, this.delayWhileIdle, this.timeToLive, this.restrictedPackageName,
					this.title, this.body, this.icon, this.sound, this.tag, this.color, clickAction, this.bodyLocKey,
					this.bodyLocArgs, this.bodyLocArgsArray, this.titleLocKey, this.titleLocArgs,
					this.titleLocArgsArray);
		}

		/**
		 * Should be used to retrieve the title from the localized resources of the app
		 * based on the user's language configuration.
		 */
		public Builder bodyLocKey(String bodyLocKey) {
			return new Builder(this.collapseKey, this.delayWhileIdle, this.timeToLive, this.restrictedPackageName,
					this.title, this.body, this.icon, this.sound, this.tag, this.color, this.clickAction, bodyLocKey,
					this.bodyLocArgs, this.bodyLocArgsArray, this.titleLocKey, this.titleLocArgs,
					this.titleLocArgsArray);
		}

		/**
		 * Serialized JSON list of body localization parameters
		 * 
		 * @deprecated use {@link Builder#bodyLocArgsArray}
		 */
		public Builder bodyLocArgs(String bodyLocArgs) {
			return new Builder(this.collapseKey, this.delayWhileIdle, this.timeToLive, this.restrictedPackageName,
					this.title, this.body, this.icon, this.sound, this.tag, this.color, this.clickAction,
					this.bodyLocKey, bodyLocArgs, this.bodyLocArgsArray, this.titleLocKey, this.titleLocArgs,
					this.titleLocArgsArray);
		}

		/**
		 * List of body localization parameters
		 */
		public Builder bodyLocArgsArray(List<String> bodyLocArgsArray) {
			return new Builder(this.collapseKey, this.delayWhileIdle, this.timeToLive, this.restrictedPackageName,
					this.title, this.body, this.icon, this.sound, this.tag, this.color, this.clickAction,
					this.bodyLocKey, this.bodyLocArgs, bodyLocArgsArray, this.titleLocKey, this.titleLocArgs,
					this.titleLocArgsArray);
		}

		/**
		 * Should be used to retrieve the title from the localized resources of the app
		 * based on the user's language configuration.
		 */
		public Builder titleLocKey(String titleLocKey) {
			return new Builder(this.collapseKey, this.delayWhileIdle, this.timeToLive, this.restrictedPackageName,
					this.title, this.body, this.icon, this.sound, this.tag, this.color, this.clickAction,
					this.bodyLocKey, this.bodyLocArgs, this.bodyLocArgsArray, titleLocKey, this.titleLocArgs,
					this.titleLocArgsArray);
		}

		/**
		 * Serialized JSON list of title localization parameters
		 * 
		 * @deprecated use {@link Builder#titleLocArgsArray}
		 */
		public Builder titleLocArgs(String titleLocArgs) {
			return new Builder(this.collapseKey, this.delayWhileIdle, this.timeToLive, this.restrictedPackageName,
					this.title, this.body, this.icon, this.sound, this.tag, this.color, this.clickAction,
					this.bodyLocKey, this.bodyLocArgs, this.bodyLocArgsArray, this.titleLocKey, titleLocArgs,
					this.titleLocArgsArray);
		}

		/**
		 * List of title localization parameters
		 */
		public Builder titleLocArgsArray(List<String> titleLocArgsArray) {
			return new Builder(this.collapseKey, this.delayWhileIdle, this.timeToLive, this.restrictedPackageName,
					this.title, this.body, this.icon, this.sound, this.tag, this.color, this.clickAction,
					this.bodyLocKey, this.bodyLocArgs, this.bodyLocArgsArray, this.titleLocKey, this.titleLocArgs,
					titleLocArgsArray);
		}

		public GcmNotification build() {
			return new GcmNotificationObject(this.collapseKey, this.delayWhileIdle, this.timeToLive,
					this.restrictedPackageName, this.title, this.body, this.icon, this.sound, this.tag, this.color,
					this.clickAction, this.bodyLocKey, this.bodyLocArgs, this.bodyLocArgsArray, this.titleLocKey,
					this.titleLocArgs, this.titleLocArgsArray);
		}

		@Getter
		@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
		private static class GcmNotificationObject implements GcmNotification {
			private final String collapseKey;
			private final Boolean delayWhileIdle;
			private final Duration timeToLive;
			private final String restrictedPackageName;
			private final String title;
			private final String body;
			private final String icon;
			private final String sound;
			private final String tag;
			private final String color;
			private final String clickAction;
			private final String bodyLocKey;
			private final String bodyLocArgs;
			private final List<String> bodyLocArgsArray;
			private final String titleLocKey;
			private final String titleLocArgs;
			private final List<String> titleLocArgsArray;
		}
	}
}

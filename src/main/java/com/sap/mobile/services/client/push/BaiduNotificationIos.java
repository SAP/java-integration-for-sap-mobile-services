package com.sap.mobile.services.client.push;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

public interface BaiduNotificationIos {

	static Builder builder() {
		return new Builder();
	}

	String getAlert();

	String getSound();

	Integer getBadge();

	Integer getDeployStatus();

	/**
	 * Baidu push provides a subset of notification properties.
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	final class Builder {
		private String alert;
		private String sound;
		private Integer badge;
		private Integer deployStatus;

		/** The notification message */
		public Builder alert(String alert) {
			return new Builder(alert, this.sound, this.badge, this.deployStatus);
		}

		/**
		 * Reference to a sound resource in the app. {@code default} will play the
		 * system sound
		 */
		public Builder sound(String sound) {
			return new Builder(this.alert, sound, this.badge, this.deployStatus);
		}

		/** Badge at app icon */
		public Builder badge(Integer badge) {
			return new Builder(this.alert, this.sound, badge, this.deployStatus);
		}

		public Builder deployStatus(Integer deployStatus) {
			return new Builder(this.alert, this.sound, this.badge, deployStatus);
		}

		public BaiduNotificationIos build() {
			return new BaiduNotificationIosObject(this.alert, this.sound, this.badge, this.deployStatus);
		}

		@Getter
		@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
		private static class BaiduNotificationIosObject implements BaiduNotificationIos {
			private final String alert;
			private final String sound;
			private final Integer badge;
			private final Integer deployStatus;
		}
	}
}

package com.sap.mobile.services.client.push;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

public interface WnsNotificationAudio {

	static Builder builder() {
		return new Builder();
	}

	Boolean getLoop();

	Boolean getSilent();

	String getSrc();

	/**
	 * Specifies a sound to play when a toast notification is displayed. This
	 * element also allows you to mute any
	 * toast notification audio.
	 * 
	 * @see <a href=
	 *      "https://docs.microsoft.com/en-us/uwp/schemas/tiles/toastschema/element-audio">https://docs.microsoft.com/en-us/uwp/schemas/tiles/toastschema/element-audio</a>
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	final class Builder {
		private Boolean loop;
		private Boolean silent;
		private String src;

		/**
		 * Set to true if the sound should repeat as long as the toast is shown; false
		 * to play only once.
		 * If this attribute is set to true, the duration attribute in the toast element
		 * must also be set.
		 * There are specific sounds provided to be used when looping. Note that UWP
		 * apps support neither
		 * looping audio nor long-duration toasts.
		 */
		public Builder loop(Boolean loop) {
			return new Builder(loop, this.silent, this.src);
		}

		/**
		 * True to mute the sound; false to allow the toast notification sound to play
		 */
		public Builder silent(Boolean silent) {
			return new Builder(this.loop, silent, this.src);
		}

		/**
		 * The media file to play in place of the default sound
		 */
		public Builder src(String src) {
			return new Builder(this.loop, this.silent, src);
		}

		public WnsNotificationAudio build() {
			return new WnsNotificationAudioObject(this.loop, this.silent, this.src);
		}

		@Getter
		@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
		private static class WnsNotificationAudioObject implements WnsNotificationAudio {
			private final Boolean loop;
			private final Boolean silent;
			private final String src;
		}
	}
}

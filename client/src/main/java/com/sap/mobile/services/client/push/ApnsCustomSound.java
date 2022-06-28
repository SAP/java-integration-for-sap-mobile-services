package com.sap.mobile.services.client.push;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * ApnsCustomSound class represents the new APNS notification sound
 * element. Compared to the legacy sound property in
 * {@link ApnsNotification#getSound()} it allows to define critical notifications
 * and the volume of critical sounds, Use the {@link Builder} to configure the
 * custom sound. Use {@link Builder} to configure and build the sound request
 * element.
 */
public interface ApnsCustomSound {

	static Builder builder() {
		return new Builder();
	}

	Boolean getCritical();

	String getSound();

	Float getVolume();

	/**
	 * Detailed APNS sound definition. Use this when sending critical notifications.
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	final class Builder {
		private Boolean critical;
		private String sound;
		private Float volume;

		/** Use the system default critical sound */
		public Builder critical(Boolean critical) {
			return new Builder(critical, this.sound, this.volume);
		}

		/**
		 * Reference to the sound resource of the app that is played when notification
		 * is received on the device. Use {@code default} for the system sound. No sound
		 * is played when this property is empty.
		 */
		public Builder sound(String sound) {
			return new Builder(this.critical, sound, this.volume);
		}

		/**
		 * The volume for the critical alert’s sound. Set this to a value between 0
		 * (silent) and 1 (full volume).
		 */
		public Builder volume(Float volume) {
			return new Builder(this.critical, this.sound, volume);
		}

		public ApnsCustomSound build() {
			return new ApnsCustomSoundObject(this.critical, this.sound, this.volume);
		}

		@Getter
		@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
		private static class ApnsCustomSoundObject implements ApnsCustomSound {
			private final Boolean critical;
			private final String sound;
			private final Float volume;
		}
	}
}

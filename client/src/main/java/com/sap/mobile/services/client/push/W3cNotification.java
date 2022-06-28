package com.sap.mobile.services.client.push;

import java.time.Duration;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * W3c-specific notification parameters. Use {@link Builder} to define and build the element.
 */
public interface W3cNotification {

	static Builder builder() {
		return new Builder();
	}

	/**
	 * Get the notification blob. May return null.
	 *
	 * @return byte array blob or null.
	 */
	byte[] getBlob();

	/**
	 * Get the time to live of the W3c notification. May return null.
	 *
	 * @return time to live or null.
	 */
	Duration getTtl();

	/**
	 * W3C specific notification element builder.
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	final class Builder {
		private byte[] blob;
		private Duration ttl;

		public Builder blob(byte[] blob) {
			return new Builder(blob, this.ttl);
		}

		public Builder ttl(Duration ttl) {
			return new Builder(this.blob, ttl);
		}

		public W3cNotification build() {
			return new W3cNotificationObject(this.blob, this.ttl);
		}

		@Getter
		@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
		private static class W3cNotificationObject implements W3cNotification {
			private final byte[] blob;
			private final Duration ttl;
		}
	}
}

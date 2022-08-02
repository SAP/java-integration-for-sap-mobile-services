package com.sap.mobile.services.client.push;

import java.util.Optional;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

public interface BaiduNotification {

	static Builder builder() {
		return new Builder();
	}

	BaiduNotificationAndroid getAndroid();

	BaiduNotificationIos getIos();

	BaiduMsgType getMsgType();

	/**
	 * Baidu notification properties
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	final class Builder {
		private BaiduNotificationAndroid android;
		private BaiduNotificationIos ios;
		private BaiduMsgType msgType;

		/** Android specific notification settings */
		public Builder android(BaiduNotificationAndroid android) {
			return new Builder(android, this.ios, this.msgType);
		}

		/** APNS specific notification settings */
		public Builder ios(BaiduNotificationIos ios) {
			return new Builder(this.android, ios, this.msgType);
		}

		/**
		 * The Baidu msgType is optional:
		 * <ul>
		 * <li>TRANSPARENT
		 * <li>NOTIFICATION visible - default
		 * </ul>
		 */
		public Builder msgType(BaiduMsgType msgType) {
			return new Builder(this.android, this.ios, msgType);
		}

		public BaiduNotification build() {
			return new BaiduNotificationObject(this.android, this.ios, this.msgType);
		}

		@Getter
		@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
		private static class BaiduNotificationObject implements BaiduNotification {
			private final BaiduNotificationAndroid android;
			private final BaiduNotificationIos ios;
			private final BaiduMsgType msgType;
		}
	}

}

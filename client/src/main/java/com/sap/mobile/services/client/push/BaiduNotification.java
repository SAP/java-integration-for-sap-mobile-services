package com.sap.mobile.services.client.push;

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
	
	/**
	 * Baidu notification properties
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	final class Builder {
		private BaiduNotificationAndroid android;
		private BaiduNotificationIos ios;
		
		/** Android specific notification settings */
		public Builder android(BaiduNotificationAndroid android) {
			return new Builder(android, this.ios);
		}
		
		/** APNS specific notification settings */
		public Builder ios(BaiduNotificationIos ios) {
			return new Builder(this.android, ios);
		}

		public BaiduNotification build() {
			return new BaiduNotificationObject(this.android, this.ios);
		}

		@Getter
		@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
		private static class BaiduNotificationObject implements BaiduNotification {
			private final BaiduNotificationAndroid android;
			private final BaiduNotificationIos ios;
		}
	}
}

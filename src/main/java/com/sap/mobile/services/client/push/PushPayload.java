/**
 * (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * No part of this publication may be reproduced or transmitted in any form or for any purpose
 * without the express permission of SAP SE. The information contained herein may be changed
 * without prior notice.
 */

package com.sap.mobile.services.client.push;

import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

public interface PushPayload {

	static Builder builder() {
		return new Builder();
	}

	String getAlert();

	Integer getBadge();

	String getSound();

	String getPriority();

	String getData();

	Boolean getSendAsSms();

	ApnsNotification getApns();

	GcmNotification getGcm();

	WnsNotification getWns();

	BaiduNotification getBaidu();

	W3cNotification getW3c();

	Map<String, String> getCustom();

	/**
	 * The generic notification definition.
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	final class Builder {
		private String alert;
		private Integer badge;
		private String sound;
		private String priority;
		private String data;
		private Boolean sendAsSms;
		private ApnsNotification apns;
		private GcmNotification gcm;
		private WnsNotification wns;
		private BaiduNotification baidu;
		private W3cNotification w3c;
		private Map<String, String> custom;

		/**
		 * Alert properties define the notification message. The push provider displays
		 * the notification message directly on the device or browser (for W3C Push).
		 * See the specific push provider section for additional information. Use plain
		 * text for alerts so the notification can be handled by multiple push
		 * providers.
		 */
		public Builder alert(String alert) {
			return new Builder(alert, this.badge, this.sound, this.priority, this.data, this.sendAsSms, this.apns,
					this.gcm, this.wns, this.baidu, this.w3c, this.custom);
		}

		/**
		 * The number to display in a badge on your app’s icon. Specify 0 to remove the
		 * current badge, if any.
		 */
		public Builder badge(Integer badge) {
			return new Builder(this.alert, badge, this.sound, this.priority, this.data, this.sendAsSms, this.apns,
					this.gcm, this.wns, this.baidu, this.w3c, this.custom);
		}

		/**
		 * When specified, play a sound when the notification arrives on the device. See
		 * the push provider for details.
		 */
		public Builder sound(String sound) {
			return new Builder(this.alert, this.badge, sound, this.priority, this.data, this.sendAsSms, this.apns,
					this.gcm, this.wns, this.baidu, this.w3c, this.custom);
		}

		/**
		 * The notification priority. Allowed values are {@code HIGH} and {@code NORMAL}
		 */
		public Builder priority(String priority) {
			return new Builder(this.alert, this.badge, this.sound, priority, this.data, this.sendAsSms, this.apns,
					this.gcm, this.wns, this.baidu, this.w3c, this.custom);
		}

		/**
		 * The data property is a serialized / escape JSON key/value map. The object is
		 * not shown in the notification, but can be used by the mobile app notification
		 * handler. Android only support maps/key value pairs and other JSON objects are
		 * ignored.
		 */
		public Builder data(String data) {
			return new Builder(this.alert, this.badge, this.sound, this.priority, data, this.sendAsSms, this.apns,
					this.gcm, this.wns, this.baidu, this.w3c, this.custom);
		}

		/** Send notification as SMS. @deprecated */
		public Builder sendAsSms(Boolean sendAsSms) {
			return new Builder(this.alert, this.badge, this.sound, this.priority, this.data, sendAsSms, this.apns,
					this.gcm, this.wns, this.baidu, this.w3c, this.custom);
		}

		/**
		 * APNS specific notification properties that overrides the PushPayload
		 * properties
		 */
		public Builder apns(ApnsNotification apns) {
			return new Builder(this.alert, this.badge, this.sound, this.priority, this.data, this.sendAsSms, apns,
					this.gcm, this.wns, this.baidu, this.w3c, this.custom);
		}

		/**
		 * FCM specific notification properties that overrides the PushPayload
		 * properties
		 */
		public Builder gcm(GcmNotification gcm) {
			return new Builder(this.alert, this.badge, this.sound, this.priority, this.data, this.sendAsSms, this.apns,
					gcm, this.wns, this.baidu, this.w3c, this.custom);
		}

		/**
		 * WNS specific notification properties that overrides the PushPayload
		 * properties
		 */
		public Builder wns(WnsNotification wns) {
			return new Builder(this.alert, this.badge, this.sound, this.priority, this.data, this.sendAsSms, this.apns,
					this.gcm, wns, this.baidu, this.w3c, this.custom);
		}

		/**
		 * Baidu specific notification properties that overrides the PushPayload
		 * properties
		 */
		public Builder baidu(BaiduNotification baidu) {
			return new Builder(this.alert, this.badge, this.sound, this.priority, this.data, this.sendAsSms, this.apns,
					this.gcm, this.wns, baidu, this.w3c, this.custom);
		}

		/**
		 * W3C specific notification properties that overrides the PushPayload
		 * properties
		 */
		public Builder w3c(W3cNotification w3c) {
			return new Builder(this.alert, this.badge, this.sound, this.priority, this.data, this.sendAsSms, this.apns,
					this.gcm, this.wns, this.baidu, w3c, this.custom);
		}

		/**
		 * Custom specific notification properties that overrides the PushPayload
		 * properties
		 */
		public Builder custom(Map<String, String> custom) {
			return new Builder(this.alert, this.badge, this.sound, this.priority, this.data, this.sendAsSms, this.apns,
					this.gcm, this.wns, this.baidu, this.w3c, custom);
		}

		public PushPayload build() {
			return new PushPayloadObject(this.alert, this.badge, this.sound, this.priority, this.data, this.sendAsSms,
					this.apns, this.gcm, this.wns, this.baidu, this.w3c, this.custom);
		}

		@Getter
		@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
		private static class PushPayloadObject implements PushPayload {
			private final String alert;
			private final Integer badge;
			private final String sound;
			private final String priority;
			private final String data;
			private final Boolean sendAsSms;
			private final ApnsNotification apns;
			private final GcmNotification gcm;
			private final WnsNotification wns;
			private final BaiduNotification baidu;
			private final W3cNotification w3c;
			private final Map<String, String> custom;
		}
	}
}

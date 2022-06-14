/**
 * (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * No part of this publication may be reproduced or transmitted in any form or for any purpose
 * without the express permission of SAP SE. The information contained herein may be changed
 * without prior notice.
 */

package com.sap.mobile.services.client.push;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * TODO doc
 */
public interface LocalizedPushPayload {

	static Builder builder() {
		return new Builder();
	}

	PushPayload getNotification();

	Map<String, ? extends PushPayload> getNotifications();

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	final class Builder {

		private PushPayload notification;
		private Map<String, ? extends PushPayload> notifications;

		public Builder notification(PushPayload notification) {
			return new Builder(notification, this.notifications);
		}

		public Builder notifications(Map<String, ? extends PushPayload> notifications) {
			return new Builder(this.notification, notifications);
		}

		public Builder addLocale(Locale locale, PushPayload pushPayload) {
			return this.addLocale(locale.toString(), pushPayload);
		}

		public Builder addLocale(String locale, PushPayload pushPayload) {
			Map<String, PushPayload> notifications = new HashMap<>(this.notifications);
			notifications.put(locale, pushPayload);
			return new Builder(this.notification, notifications);
		}

		public LocalizedPushPayload build() {
			return new LocalizedPushPayloadObject(this.notification, this.notifications);
		}

		@Getter
		@RequiredArgsConstructor
		private static class LocalizedPushPayloadObject implements LocalizedPushPayload {
			private final PushPayload notification;
			private final Map<String, ? extends PushPayload> notifications;
		}
	}
}

/**
 * (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * No part of this publication may be reproduced or transmitted in any form or for any purpose
 * without the express permission of SAP SE. The information contained herein may be changed
 * without prior notice.
 */

package com.sap.mobile.services.client.push;

import java.util.Collection;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * TODO doc
 */
public interface LocalizedBulkPush {

	static Builder builder() {
		return new Builder();
	}

	LocalizedPushPayload getNotification();

	Collection<LocalizedUserNotification> getUserNotifications();

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	final class Builder {
		private LocalizedPushPayload notification;
		private Collection<LocalizedUserNotification> userNotifications;

		public Builder notification(LocalizedPushPayload notification) {
			return new Builder(notification, this.userNotifications);
		}

		public Builder userNotifications(Collection<LocalizedUserNotification> userNotifications) {
			return new Builder(this.notification, userNotifications);
		}

		public LocalizedBulkPush build() {
			return new LocalizedBulkPushObject(this.notification, this.userNotifications);
		}

		@Getter
		@RequiredArgsConstructor
		private static final class LocalizedBulkPushObject implements LocalizedBulkPush {
			private final LocalizedPushPayload notification;
			private final Collection<LocalizedUserNotification> userNotifications;
		}
	}
}

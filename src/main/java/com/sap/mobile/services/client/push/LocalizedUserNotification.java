/**
 * (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * No part of this publication may be reproduced or transmitted in any form or for any purpose
 * without the express permission of SAP SE. The information contained herein may be changed
 * without prior notice.
 */

package com.sap.mobile.services.client.push;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * TODO doc
 */
public interface LocalizedUserNotification {

	static Builder builder() {
		return new Builder();
	}

	String getUser();

	LocalizedPushPayload getNotification();

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	final class Builder {
		private String user;
		private LocalizedPushPayload notification;

		public Builder user(String user) {
			return new Builder(user, this.notification);
		}

		public Builder notification(LocalizedPushPayload notification) {
			return new Builder(this.user, notification);
		}

		public LocalizedUserNotification build() {
			return new LocalizedUserNotificationObject(this.user, this.notification);
		}

		@Getter
		@RequiredArgsConstructor
		private static class LocalizedUserNotificationObject implements LocalizedUserNotification {
			private final String user;
			private final LocalizedPushPayload notification;
		}
	}
}

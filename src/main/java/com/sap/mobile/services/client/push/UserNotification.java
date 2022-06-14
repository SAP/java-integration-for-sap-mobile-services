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

public interface UserNotification {

	static Builder builder() {
		return new Builder();
	}

	String getUser();

	PushPayload getNotification();

	/**
	 * Bulk push notification payload for a specific user.
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	final class Builder {
		private String user;
		private PushPayload notification;

		/** Username */
		public Builder user(String user) {
			return new Builder(user, this.notification);
		}

		/** Specific push payload */
		public Builder notification(PushPayload notification) {
			return new Builder(this.user, notification);
		}

		public UserNotification build() {
			return new UserNotificationObject(this.user, this.notification);
		}

		@Getter
		@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
		private static class UserNotificationObject implements UserNotification {
			private final String user;
			private final PushPayload notification;
		}
	}
}

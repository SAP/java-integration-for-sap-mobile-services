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

public interface WnsNotificationCommandsCommand {

	static Builder builder() {
		return new Builder();
	}

	String getId();

	String getArguments();

	/**
	 * Specifies a scenario-associated button shown. The scenario is specified in
	 * the parent commands element.
	 * 
	 * @see <a href=
	 *      "https://docs.microsoft.com/en-us/uwp/schemas/tiles/toastschema/element-command">https://docs.microsoft.com/en-us/uwp/schemas/tiles/toastschema/element-command</a>
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	final class Builder {
		private String id;
		private String arguments;

		/**
		 * Specifies one command from the system-defined command list. These values
		 * correspond to available actions that the user can take.
		 */
		public Builder id(String id) {
			return new Builder(id, this.arguments);
		}

		/**
		 * An argument string that can be passed to the associated app to provide
		 * specifics about the action that it
		 * should execute in response to the user action.
		 */
		public Builder arguments(String arguments) {
			return new Builder(this.id, arguments);
		}

		public WnsNotificationCommandsCommand build() {
			return new WnsNotificationCommandsCommandObject(this.id, this.arguments);
		}

		@Getter
		@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
		private static class WnsNotificationCommandsCommandObject implements WnsNotificationCommandsCommand {
			private final String id;
			private final String arguments;
		}
	}
}

package com.sap.mobile.services.client.push;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

public interface WnsNotificationCommands {

	static Builder builder() {
		return new Builder();
	}

	String getScenario();

	List<WnsNotificationCommandsCommand> getCommand();

	/**
	 * Specifies that the notification is being used to indicate an incoming call or
	 * an alarm, with appropriate
	 * commands associated with each scenario.
	 * 
	 * @see <a href=
	 *      "https://docs.microsoft.com/en-us/uwp/schemas/tiles/toastschema/element-commands">https://docs.microsoft.com/en-us/uwp/schemas/tiles/toastschema/element-commands</a>
	 */

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	final class Builder {
		private String scenario;
		private List<WnsNotificationCommandsCommand> command;

		public Builder scenario(String scenario) {
			return new Builder(scenario, this.command);
		}

		/**
		 * Specifies a scenario-associated button shown. The scenario is specified in
		 * the parent commands element.
		 */
		public Builder command(List<WnsNotificationCommandsCommand> command) {
			return new Builder(this.scenario, command);
		}

		/**
		 * The intended use of the notification.
		 */
		public WnsNotificationCommands build() {
			return new WnsNotificationCommandsObject(this.scenario, this.command);
		}

		@Getter
		@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
		private static class WnsNotificationCommandsObject implements WnsNotificationCommands {
			private final String scenario;
			private final List<WnsNotificationCommandsCommand> command;
		}
	}
}

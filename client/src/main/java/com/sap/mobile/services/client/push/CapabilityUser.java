package com.sap.mobile.services.client.push;

import java.util.Collection;
import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * The CapabilityUser entity is a filter element to select the target devices.
 * The mandatory filter values are the username and form factor collection. Both
 * parameters are required.
 * Use {@link Builder} to define and build the request payload.
 */
public interface CapabilityUser {

	static Builder builder() {
		return new Builder();
	}

	Integer getBadge();

	Map<String, Integer> getBadges();

	Collection<String> getFormFactor();

	String getUser();

	/**
	 * Defines targets for
	 * {@link PushClient#pushToCapability(String, PushToCapabilitiesPayload)}
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	final class Builder {
		private Integer badge;
		private Map<String, Integer> badges;
		private Collection<String> formFactor;
		private String user;

		/** Badge value for selected target */
		public Builder badge(Integer badge) {
			return new Builder(badge, this.badges, this.formFactor, this.user);
		}

		/** Badge values for specific registration IDs */
		public Builder badges(Map<String, Integer> badges) {
			return new Builder(this.badge, badges, this.formFactor, this.user);
		}

		/** Mandatory form factor: {@code phone tablet} */
		public Builder formFactor(Collection<String> formFactor) {
			return new Builder(this.badge, this.badges, formFactor, this.user);
		}

		/** Mandatory username */
		public Builder user(String user) {
			return new Builder(this.badge, this.badges, this.formFactor, user);
		}

		public CapabilityUser build() {
			return new CapabilityUserObject(badge, badges, formFactor, user);
		}

		@Getter
		@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
		private static class CapabilityUserObject implements CapabilityUser {
			private final Integer badge;
			private final Map<String, Integer> badges;
			private final Collection<String> formFactor;
			private final String user;
		}
	}
}

package com.sap.mobile.services.client.push;

import java.util.Collection;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * Root payload element for push service localized
 * push to capability, @see <a target="_top"
 * href=
 * "https://help.sap.com/doc/f53c64b93e5140918d676b927a3cd65b/Cloud/en-US/docs-en/guides/features/push/api/localization.html#push-service-based-localization">Push
 * Service Based Localization</a>. Use the {@link Builder} to define and build
 * the request.
 */
public interface LocalizedPushToCapabilitiesPayload {
	static Builder builder() {
		return new Builder();
	}

	Collection<CapabilityUser> getUsers();

	LocalizedPushPayload getNotification();

	/**
	 * Helper to define and create the localized push to capability request. @see
	 * <a target="_top" href=
	 * "https://help.sap.com/doc/f53c64b93e5140918d676b927a3cd65b/Cloud/en-US/docs-en/guides/features/push/api/localization.html#push-service-based-localization">Push
	 * Service Based Localization</a>.
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	final class Builder {
		private Collection<CapabilityUser> users;
		private LocalizedPushPayload notification;

		public Builder users(Collection<CapabilityUser> users) {
			return new Builder(users, this.notification);
		}

		public Builder notification(LocalizedPushPayload notification) {
			return new Builder(this.users, notification);
		}

		public LocalizedPushToCapabilitiesPayload build() {
			return new LocalizedPushToCapabilitiesPayloadObject(this.users, this.notification);
		}

		@Getter
		@RequiredArgsConstructor
		private static class LocalizedPushToCapabilitiesPayloadObject implements LocalizedPushToCapabilitiesPayload {
			private final Collection<CapabilityUser> users;
			private final LocalizedPushPayload notification;
		}
	}
}

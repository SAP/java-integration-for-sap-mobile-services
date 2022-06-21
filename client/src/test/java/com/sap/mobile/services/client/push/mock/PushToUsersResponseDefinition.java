package com.sap.mobile.services.client.push.mock;

import org.springframework.http.HttpStatus;

public class PushToUsersResponseDefinition extends ResponseDefinition<PushToUsersResponseDefinition> {

	public PushToUsersResponseDefinition(final MockPushServer mockPushServer) {
		super(mockPushServer);
		this.withJsonBodyFromResource("payloads/response-push-to-users-success.json");
	}

	public PushToUsersResponseDefinition withInvalidPushUserListSizeError() {
		return this.withStatus(HttpStatus.BAD_REQUEST)
				.withJsonBodyFromResource("payloads/response-push-to-users-invalid-user-list-size.json");
	}

	public PushToUsersResponseDefinition withNoSuchRegistrationError() {
		return this.withStatus(HttpStatus.NOT_FOUND)
				.withJsonBodyFromResource("payloads/response-push-to-users-no-such-registration.json");
	}
}

package com.sap.mobile.services.client.push.mock;

import org.springframework.http.HttpStatus;

public class PushToUserResponseDefinition extends ResponseDefinition<PushToUserResponseDefinition> {

	public PushToUserResponseDefinition(final MockPushServer mockPushServer) {
		super(mockPushServer);
		this.withJsonBodyFromResource("payloads/response-push-to-user-success.json");
	}

	public PushToUserResponseDefinition withNoSuchRegistrationError() {
		return this.withStatus(HttpStatus.NOT_FOUND)
				.withJsonBodyFromResource("payloads/response-push-to-user-no-such-registration.json");
	}
}

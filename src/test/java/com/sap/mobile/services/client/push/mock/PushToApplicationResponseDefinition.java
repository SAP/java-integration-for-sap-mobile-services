package com.sap.mobile.services.client.push.mock;

public class PushToApplicationResponseDefinition extends ResponseDefinition<PushToApplicationResponseDefinition> {

	public PushToApplicationResponseDefinition(final MockPushServer mockPushServer) {
		super(mockPushServer);
		this.withJsonBodyFromResource("payloads/response-push-to-application-success.json");
	}
}

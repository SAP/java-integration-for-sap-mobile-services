/**
 * (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * No part of this publication may be reproduced or transmitted in any form or for any purpose
 * without the express permission of SAP SE. The information contained herein may be changed
 * without prior notice.
 */

package com.sap.mobile.services.client.push;

public class PushClientTest {
	public void testCompilation() throws Exception {
		PushClient pushClient = new PushClientBuilder()
				.build(MobileServicesSettings.fromResource("mobileservices-config.json"));

		ApnsNotification apns = ApnsNotification.builder().alertBody("alert_body").build();
		PushPayload pushPayload = PushPayload.builder().alert("alert").apns(apns).build();

		pushClient.pushToApplication(pushPayload);
	}
}

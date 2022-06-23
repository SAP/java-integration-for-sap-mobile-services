package com.sap.mobile.services.client.push;

import feign.Param;
import feign.RequestLine;

public interface Push {
	@RequestLine("POST " + Constants.Backend.V2.Paths.PUSH_TO_APPLICATION_PATH)
	DTOPushResponse pushToApplication(DTOLocalizedPushPayload payload, @Param("applicationId") String applicationId);
}

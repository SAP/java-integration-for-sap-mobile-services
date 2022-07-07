package com.sap.mobile.services.client.validation.broker.service.api;

import java.util.Map;
import java.util.Set;

import com.sap.mobile.services.client.validation.broker.exception.InstanceCreationFailedException;
import com.sap.mobile.services.client.validation.broker.exception.InstanceCreationTimeoutException;
import com.sap.mobile.services.client.validation.broker.exception.MaxConcurrentInstancesReachedException;
import com.sap.mobile.services.client.validation.broker.exception.NoSuchServiceInstanceException;

public interface BrokerService {

	Map<String, ?> createMobileApplication(final Set<String> requestFeatures) throws MaxConcurrentInstancesReachedException, InstanceCreationFailedException, InstanceCreationTimeoutException;

	Map<String, ?> getMobileApplicationKey(final String appId) throws NoSuchServiceInstanceException;

	void deleteMobileApplication(final String appId) throws NoSuchServiceInstanceException;

}

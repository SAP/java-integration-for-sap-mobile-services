package com.sap.mobile.services.client.push;

public interface PushRegistration {
	String getId();

	String getDeviceId();

	String getDeviceModel();

	FormFactor getFormFactor();

	String getGroup();

	Provider getProvider();

	String getLocale();

	String getUsername();
}

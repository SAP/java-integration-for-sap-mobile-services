package com.sap.mobile.services.client.push;

/**
 * Response from {@link PushClient#getNotificationStatus(String)}
 */
public interface NotificationStatusResponse {


	StatusResponseStatus getStatus();

	NotificationStatus getStatusDetails();

}

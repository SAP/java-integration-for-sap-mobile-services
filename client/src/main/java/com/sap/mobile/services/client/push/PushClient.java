package com.sap.mobile.services.client.push;

import java.util.Collection;
import java.util.Set;

import com.sap.mobile.services.client.ClientException;

/**
 * The push client is used to send the notification and retrieve the
 * notification status.
 */
public interface PushClient {

	/**
	 * Triggers a notification to all registered users of an application.
	 *
	 * @param pushPayload payload
	 * @return response
	 */
	PushResponse pushToApplication(PushPayload pushPayload) throws ClientException;

	/**
	 * Triggers a notification to all registered users of an application.
	 *
	 * @param pushPayload payload
	 * @return response
	 */
	PushResponse pushToApplication(LocalizedPushPayload pushPayload) throws ClientException;

	/**
	 * Triggers a notification to a single device, identified by userId and
	 * deviceId.
	 *
	 * @param userId      userId
	 * @param deviceId    deviceId
	 * @param pushPayload payload
	 * @return response
	 */
	PushResponse pushToDevice(String userId, String deviceId, PushPayload pushPayload) throws ClientException;

	/**
	 * Triggers a notification to a single device, identified by userId and
	 * deviceId.
	 *
	 * @param userId      userId
	 * @param deviceId    deviceId
	 * @param pushPayload payload
	 * @return response
	 */
	PushResponse pushToDevice(String userId, String deviceId, LocalizedPushPayload pushPayload) throws ClientException;

	/**
	 * Triggers a notification to a defined set of users, identified by their
	 * userId. If a single user has multiple
	 * devices registered, all devices will receive the notification.
	 *
	 * @param userIds     userIds
	 * @param pushPayload payload
	 * @return response
	 */
	PushResponse pushToUsers(Collection<String> userIds, PushPayload pushPayload) throws ClientException;

	/**
	 * Triggers a notification to a defined set of users, identified by their
	 * userId. If a single user has multiple
	 * devices registered, all devices will receive the notification.
	 *
	 * @param userIds     userIds
	 * @param pushPayload payload
	 * @return response
	 */
	PushResponse pushToUsers(Collection<String> userIds, LocalizedPushPayload pushPayload) throws ClientException;

	/**
	 * Triggers a notification to a group with the same payload for all recipients.
	 *
	 * @param group       group
	 * @param pushPayload payload
	 * @return response
	 */
	PushResponse pushToGroup(String group, PushPayload pushPayload) throws ClientException;

	/**
	 * Triggers a notification to a group with the same payload for all recipients.
	 *
	 * @param group       group
	 * @param pushPayload payload
	 * @return response
	 */
	PushResponse pushToGroup(String group, LocalizedPushPayload pushPayload) throws ClientException;

	/**
	 * Triggers a bulk of notifications. The notifications are temporarily queued
	 * before handed over to the push providers
	 *
	 * @param rootNotification  fallback notification object
	 * @param userNotifications user notification objects
	 * @return response
	 */
	PushResponse bulkPush(PushPayload rootNotification, Collection<UserNotification> userNotifications);

	/**
	 * Triggers notifications to device registrations with a capability and form
	 * factor.
	 *
	 * @param rootNotification  fallback notification object
	 * @param userNotifications user notification objects
	 * @return response
	 */
	PushResponse bulkPush(LocalizedPushPayload rootNotification,
			Collection<LocalizedUserNotification> userNotifications);

	/**
	 * Triggers notifications to devices that have been registered with a certain capability.
	 *
	 * @param capability                capability
	 * @param pushToCapabilitiesPayload payload
	 * @return response
	 */
	PushResponse pushToCapability(String capability, PushToCapabilitiesPayload pushToCapabilitiesPayload)
			throws ClientException;

	/**
	 * Triggers notifications to devices that have been registered with a certain capability.
	 *
	 * @param capability                capability
	 * @param pushToCapabilitiesPayload payload
	 * @return response
	 */
	PushResponse pushToCapability(String capability, LocalizedPushToCapabilitiesPayload pushToCapabilitiesPayload)
			throws ClientException;

	/**
	 * Triggers notifications to devices that have an active subscription for a
	 * topic
	 * 
	 * @param userIds     optional filter on userIds
	 * @param topics      List of topics
	 * @param pushPayload payload
	 * @return
	 */
	PushResponse pushToTopic(Collection<String> userIds, Collection<String> topics, LocalizedPushPayload pushPayload);

	/**
	 * Retrieve the notification status by its ID.
	 *
	 * @param notificationId notificationId
	 * @return response
	 */
	NotificationStatusResponse getNotificationStatus(String notificationId);

	/**
	 * Retrieve the set of locales for a set of user ids and their registered locale. If userIds is null, all registered
	 * locales are returned.
	 *
	 * @param userIds userIds
	 * @return set of locales
	 */
	Set<String> getLocalizations(Collection<String> userIds);
}

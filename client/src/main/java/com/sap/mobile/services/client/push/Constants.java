package com.sap.mobile.services.client.push;

interface Constants {
	/**
	 * Mobile services push root path
	 */
	String ROOT_PATH = "/mobileservices/push";

	interface Backend {

		interface V2 {
			interface Paths {
				/**
				 * Root path of mobile services puch backend services v2.
				 */
				String ROOT_PATH = Constants.ROOT_PATH + "/v2/backend";

				/**
				 * Push to application v2 path.
				 */
				String PUSH_TO_APPLICATION_PATH = Constants.Backend.V2.Paths.ROOT_PATH
						+ "/applications/{applicationId}/notifications";

				String PUSH_TO_USER_PATH = Constants.Backend.V2.Paths.ROOT_PATH
						+ "/applications/{applicationId}/users/{userId}/devices/{deviceId}/notifications";

				String PUSH_TO_GROUP_PATH = Constants.Backend.V2.Paths.ROOT_PATH
						+ "/applications/{applicationId}/groups/{group}/notifications";

				String PUSH_TO_USERS_PATH = Constants.Backend.V2.Paths.ROOT_PATH
						+ "/applications/{applicationId}/notifications/users";

				String BULK_PUSH_PATH = Constants.Backend.V2.Paths.ROOT_PATH
						+ "/applications/{applicationId}/notifications/bulk";

				String PUSH_TO_TOPIC_PATH = Constants.Backend.V2.Paths.ROOT_PATH
						+ "/applications/{applicationId}/topics/notifications";

				String PUSH_TO_CAPABILITY_PATH = Constants.Backend.V2.Paths.ROOT_PATH
						+ "/capabilities/{capabilityName}/notifications";

				String GET_NOTIFICATION_STATUS = Constants.Backend.V2.Paths.ROOT_PATH
						+ "/notifications/{notificationId}/status";

				String GET_LOCALIZATIONS = Constants.Backend.V2.Paths.ROOT_PATH + "/localizations";
			}

			interface Params {
				String GET_LOCALIZATIONS_USER_PARAM = "usernames";
			}
		}
	}
}

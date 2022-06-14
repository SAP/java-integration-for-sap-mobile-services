package com.sap.mobile.services.client.push;

interface Constants {

	interface Params {
		interface Backend {
			interface V2 {
				String GET_LOCALIZATIONS_USER_PARAM = "usernames";
			}
		}
	}

	interface Paths {
		/**
		 * Mobile services push root path
		 */
		String ROOT_PATH = "/mobileservices/push";

		interface Backend {

			interface V1 {
				/**
				 * Root path of mobile services push backend services v1.
				 */
				String ROOT_PATH = Constants.Paths.ROOT_PATH + "/v1/backend";

				/**
				 * Push to application v1 path.
				 */
				String PUSH_TO_APPLICATION_PATH = Constants.Paths.Backend.V1.ROOT_PATH
						+ "/applications/{applicationId}/notifications";

				String PUSH_TO_USER_PATH = Constants.Paths.Backend.V1.ROOT_PATH
						+ "/applications/{applicationId}/users/{userId}/devices/{deviceId}/notifications";

				String PUSH_TO_GROUP_PATH = Constants.Paths.Backend.V1.ROOT_PATH
						+ "/applications/{applicationId}/groups/{group}/notifications";

				String PUSH_TO_USERS_PATH = Constants.Paths.Backend.V1.ROOT_PATH
						+ "/applications/{applicationId}/notifications/users";

				String BULK_PUSH_PATH = Constants.Paths.Backend.V1.ROOT_PATH
						+ "/applications/{applicationId}/notifications/bulk";

				String PUSH_TO_CAPABILITY_PATH = Constants.Paths.Backend.V1.ROOT_PATH
						+ "/capabilities/{capabilityName}/notifications";

				String GET_NOTIFICATION_STATUS = Constants.Paths.Backend.V1.ROOT_PATH
						+ "/notifications/{notificationId}/status";
			}

			interface V2 {
				/**
				 * Root path of mobile services puch backend services v2.
				 */
				String ROOT_PATH = Paths.ROOT_PATH + "/v2/backend";

				/**
				 * Push to application v1 path.
				 */
				String PUSH_TO_APPLICATION_PATH = Constants.Paths.Backend.V2.ROOT_PATH
						+ "/applications/{applicationId}/notifications";

				String PUSH_TO_USER_PATH = Constants.Paths.Backend.V2.ROOT_PATH
						+ "/applications/{applicationId}/users/{userId}/devices/{deviceId}/notifications";

				String PUSH_TO_GROUP_PATH = Constants.Paths.Backend.V2.ROOT_PATH
						+ "/applications/{applicationId}/groups/{group}/notifications";

				String PUSH_TO_USERS_PATH = Constants.Paths.Backend.V2.ROOT_PATH
						+ "/applications/{applicationId}/notifications/users";

				String BULK_PUSH_PATH = Constants.Paths.Backend.V2.ROOT_PATH
						+ "/applications/{applicationId}/notifications/bulk";

				String PUSH_TO_CAPABILITY_PATH = Constants.Paths.Backend.V2.ROOT_PATH
						+ "/capabilities/{capabilityName}/notifications";

				String GET_NOTIFICATION_STATUS = Constants.Paths.Backend.V2.ROOT_PATH
						+ "/notifications/{notificationId}/status";

				String GET_LOCALIZATIONS = Constants.Paths.Backend.V2.ROOT_PATH + "/localizations";
			}
		}
	}

	interface Binding {
		String MOBILE_SERVICES_ENDPOINT_NAME = "mobileservices";
	}

	interface Headers {
		String SERVICE_KEY_AUTH_HEADER = "X-API-Key";
		String API_WARN_HEADER = "X-API-Warn";
		String CLIENT_VERSION_HEADER_NAME = "SAP-MS-Client-Version";
		String CLIENT_LANGUAGE_HEADER_NAME = "SAP-MS-Client-Language";
		String TENANT_ID_HEADER = "x-tenant-id";
	}
}

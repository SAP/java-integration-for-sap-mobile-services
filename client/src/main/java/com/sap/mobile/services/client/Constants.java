package com.sap.mobile.services.client;

interface Constants {

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

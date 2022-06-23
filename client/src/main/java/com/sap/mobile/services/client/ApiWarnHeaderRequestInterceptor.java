package com.sap.mobile.services.client;

import lombok.extern.slf4j.Slf4j;

/**
 * This interceptor does a warn logging, once the X-API-Warn header is present, which is used for deprecated APIs.
 */
@Slf4j
class ApiWarnHeaderRequestInterceptor {
	// TODO find out how this can be done in feign
}

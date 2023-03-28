package com.sap.mobile.services.client.push;

import org.springframework.http.HttpHeaders;

import com.sap.mobile.services.client.ClientException;

import lombok.Getter;
/**
 * Generic Push Client Exception with Push Response.
 */
@Getter
public class PushClientException extends ClientException {

    /**
     * Response from push notification service
     */
    PushResponse response;

    protected PushClientException(String msg, String responseBodyText, HttpHeaders httpHeaders) {
        super(msg, responseBodyText, httpHeaders);
    }

    public PushClientException(String msg, String responseBodyText, HttpHeaders httpHeaders,
            PushResponse response) {
        super(msg, responseBodyText, httpHeaders);
        this.response = response;
    }

}

package com.sap.mobile.services.client.push;

import org.springframework.http.HttpHeaders;

/**
 * Exception indicating issues with the push request, see getResponse
 * for details. Most likely, the registered device token is invalid.
 */
public class MessageErrorException extends PushClientException {

    MessageErrorException(String responseBodyText, HttpHeaders headers,
            PushResponse pushResponse) {
        super("Error in notification request. No message sent.", responseBodyText, headers, pushResponse);
    }

    public MessageErrorException(String responseBodyText, HttpHeaders headers) {
        super("Error in notification request. No message sent.", responseBodyText, headers);
    }

}

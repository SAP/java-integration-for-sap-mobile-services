package com.sap.mobile.services.client.push;

import com.sap.mobile.services.client.HttpHeaders;

/**
 * Exception indicating issues with the push request, see getResponse
 * for details. Most likely, the registered device token is invalid.
 */
public class MessageErrorException extends PushClientException {

    MessageErrorException(String responseBodyText, HttpHeaders httpHeaders,
            PushResponse pushResponse) {
        super("Error in notification request. No message sent.", responseBodyText, httpHeaders, pushResponse);
    }

    public MessageErrorException(String responseBodyText, HttpHeaders httpHeaders) {
        super("Error in notification request. No message sent.", responseBodyText, httpHeaders);
    }

}

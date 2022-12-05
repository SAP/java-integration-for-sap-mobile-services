package com.sap.mobile.services.client.push;

import java.util.List;

/**
 * List of matching topics response of {@link NotificationStatus}
 */
public interface Topics {

    /** Count of matching topics for this notification */
    Integer getCount();

    /** List of matching topics */
    List<String> getValue();

}

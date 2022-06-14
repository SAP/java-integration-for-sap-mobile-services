/**
 * (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * No part of this publication may be reproduced or transmitted in any form or for any purpose
 * without the express permission of SAP SE. The information contained herein may be changed
 * without prior notice.
 */

package com.sap.mobile.services.client.push;

/**
 * Global notification priority. The notification priority affects APNS and FCM
 * notifications.
 * APNS background {@link ApnsNotification#getContentAvailable()} notifications
 * will override the priority with NORMAL
 */
public enum Priority {
	NORMAL, HIGH
}

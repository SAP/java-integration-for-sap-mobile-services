/**
 * (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * No part of this publication may be reproduced or transmitted in any form or for any purpose
 * without the express permission of SAP SE. The information contained herein may be changed
 * without prior notice.
 */

package com.sap.mobile.services.client.push;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

public interface WnsNotificationImage {

	static Builder builder() {
		return new Builder();
	}

	String getAlt();

	String getSrc();

	Boolean getAddImageQuery();

	/**
	 * Specifies an image used in the toast template.
	 * 
	 * @see <a href=
	 *      "https://docs.microsoft.com/en-us/uwp/schemas/tiles/toastschema/element-image">https://docs.microsoft.com/en-us/uwp/schemas/tiles/toastschema/element-image</a>
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	final class Builder {
		private String alt;
		private String src;
		private Boolean addImageQuery;

		/**
		 * A description of the image, for users of assistive technologies.
		 */
		public Builder alt(String alt) {
			return new Builder(alt, this.src, this.addImageQuery);
		}

		/**
		 * The URI of the image source.
		 */
		public Builder src(String src) {
			return new Builder(this.alt, src, this.addImageQuery);
		}

		/**
		 * Set to "true" to allow Windows to append a query string to the image URI
		 * supplied in the toast notification.
		 * Use this attribute if your server hosts images and can handle query strings,
		 * either by retrieving an image
		 * variant based on the query strings or by ignoring the query string and
		 * returning the image as
		 * specified without the query string.
		 */
		public Builder addImageQuery(Boolean addImageQuery) {
			return new Builder(this.alt, this.src, addImageQuery);
		}

		public WnsNotificationImage build() {
			return new WnsNotificationImageObject(this.alt, this.src, this.addImageQuery);
		}

		@Getter
		@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
		private static class WnsNotificationImageObject implements WnsNotificationImage {
			private final String alt;
			private final String src;
			private final Boolean addImageQuery;
		}
	}
}

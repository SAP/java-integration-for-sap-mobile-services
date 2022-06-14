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

public interface BaiduNotificationAndroid {

	static Builder builder() {
		return new Builder();
	}

	String getTitle();

	String getDescription();

	Integer getNotificationBuilderId();

	Integer getNotificationBasicStyle();

	Integer getOpenType();

	String getUrl();

	String getPkgContent();

	/**
	 * Baidu push supports a subset of Android notification settings.
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	final class Builder {
		private String title;
		private String description;
		private Integer notificationBuilderId;
		private Integer notificationBasicStyle;
		private Integer openType;
		private String url;
		private String pkgContent;

		/** Notification tile */
		public Builder title(String title) {
			return new Builder(title, this.description, this.notificationBuilderId, this.notificationBasicStyle,
					this.openType, this.url, this.pkgContent);
		}

		public Builder description(String description) {
			return new Builder(this.title, description, this.notificationBuilderId, this.notificationBasicStyle,
					this.openType, this.url, this.pkgContent);
		}

		public Builder notificationBuilderId(Integer notificationBuilderId) {
			return new Builder(this.title, this.description, notificationBuilderId, this.notificationBasicStyle,
					this.openType, this.url, this.pkgContent);
		}

		public Builder notificationBasicStyle(Integer notificationBasicStyle) {
			return new Builder(this.title, this.description, this.notificationBuilderId, notificationBasicStyle,
					this.openType, this.url, this.pkgContent);
		}

		public Builder openType(Integer openType) {
			return new Builder(this.title, this.description, this.notificationBuilderId, this.notificationBasicStyle,
					openType, this.url, this.pkgContent);
		}

		public Builder url(String url) {
			return new Builder(this.title, this.description, this.notificationBuilderId, this.notificationBasicStyle,
					this.openType, url, this.pkgContent);
		}

		public Builder pkgContent(String pkgContent) {
			return new Builder(this.title, this.description, this.notificationBuilderId, this.notificationBasicStyle,
					this.openType, this.url, pkgContent);
		}

		public BaiduNotificationAndroid build() {
			return new BaiduNotificationAndroidObject(this.title, this.description, this.notificationBuilderId,
					this.notificationBasicStyle, this.openType, this.url, this.pkgContent);
		}

		@Getter
		@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
		private static class BaiduNotificationAndroidObject implements BaiduNotificationAndroid {
			private final String title;
			private final String description;
			private final Integer notificationBuilderId;
			private final Integer notificationBasicStyle;
			private final Integer openType;
			private final String url;
			private final String pkgContent;
		}
	}
}

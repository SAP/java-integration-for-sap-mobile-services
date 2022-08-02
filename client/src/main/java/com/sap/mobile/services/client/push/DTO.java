package com.sap.mobile.services.client.push;

import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

enum DTOPushType {
	ALERT, BACKGROUND, VOIP, COMPLICATION, FILEPROVIDER, MDM;

	static DTOPushType of(PushType pushType) {
		return Utils.safeMap(Utils.safeMap(pushType, PushType::name), DTOPushType::valueOf);
	}
}

@Getter
@JsonInclude(Include.NON_NULL)
class DTOApnsCustomSound {
	private final Boolean critical;
	private final String sound;
	private final Float volume;

	DTOApnsCustomSound(ApnsCustomSound apnsCustomSound) {
		this.critical = apnsCustomSound.getCritical();
		this.sound = apnsCustomSound.getSound();
		this.volume = apnsCustomSound.getVolume();
	}
}

@Getter
@JsonInclude(Include.NON_NULL)
class DTOApnsNotification {
	private final Date expiration;
	private final String category;
	private final Boolean contentAvailable;
	private final DTOPushType pushType;
	private final String customValues;
	private final String sound;
	private final DTOApnsCustomSound customSound;
	private final String topic;
	private final String alertBody;
	private final String localizedAlertKey;
	private final List<String> localizedAlertArguments;
	private final String alertTitle;
	private final String localizedAlertTitleKey;
	private final List<String> localizedAlertTitleArguments;
	private final String alertSubtitle;
	private final String localizedAlertSubtitleKey;
	private final List<String> localizedAlertSubtitleArguments;
	private final String launchImageFileName;
	private final Boolean showActionButton;
	private final String actionButtonLabel;
	private final String localizedActionButtonKey;
	private final List<String> urlArguments;
	private final String threadId;
	private final Boolean mutableContent;

	DTOApnsNotification(ApnsNotification apnsNotification) {
		this.expiration = apnsNotification.getExpiration();
		this.category = apnsNotification.getCategory();
		this.contentAvailable = apnsNotification.getContentAvailable();
		this.pushType = DTOPushType.of(apnsNotification.getPushType());
		this.customValues = apnsNotification.getCustomValues();
		this.sound = apnsNotification.getSound();
		this.customSound = Utils.safeMap(apnsNotification.getCustomSound(), DTOApnsCustomSound::new);
		this.topic = apnsNotification.getTopic();
		this.alertBody = apnsNotification.getAlertBody();
		this.localizedAlertKey = apnsNotification.getLocalizedAlertKey();
		this.localizedAlertArguments = apnsNotification.getLocalizedAlertArguments();
		this.alertTitle = apnsNotification.getAlertTitle();
		this.localizedAlertTitleKey = apnsNotification.getLocalizedAlertTitleKey();
		this.localizedAlertTitleArguments = apnsNotification.getLocalizedAlertTitleArguments();
		this.alertSubtitle = apnsNotification.getAlertSubtitle();
		this.localizedAlertSubtitleKey = apnsNotification.getLocalizedAlertSubtitleKey();
		this.localizedAlertSubtitleArguments = apnsNotification.getLocalizedAlertSubtitleArguments();
		this.launchImageFileName = apnsNotification.getLaunchImageFileName();
		this.showActionButton = apnsNotification.getShowActionButton();
		this.actionButtonLabel = apnsNotification.getActionButtonLabel();
		this.localizedActionButtonKey = apnsNotification.getLocalizedActionButtonKey();
		this.urlArguments = apnsNotification.getUrlArguments();
		this.threadId = apnsNotification.getThreadId();
		this.mutableContent = apnsNotification.getMutableContent();
	}
}

@Getter
@JsonInclude(Include.NON_NULL)
class DTOBaiduNotification {
	private final DTOBaiduNotificationAndroid android;
	private final DTOBaiduNotificationIos ios;
	private final Integer msgType;

	DTOBaiduNotification(BaiduNotification baiduNotification) {
		this.android = Optional.ofNullable(baiduNotification.getAndroid()).map(DTOBaiduNotificationAndroid::new)
				.orElse(null);
		this.ios = Optional.ofNullable(baiduNotification.getIos()).map(DTOBaiduNotificationIos::new).orElse(null);
		this.msgType = Optional.ofNullable(baiduNotification.getMsgType()).map(BaiduMsgType::ordinal)
				.orElse(null);
	}
}

@Getter
@JsonInclude(Include.NON_NULL)
class DTOBaiduNotificationAndroid {
	private final String title;
	private final String description;
	private final Integer notificationBuilderId;
	private final Integer notificationBasicStyle;
	private final Integer openType;
	private final String url;
	private final String pkgContent;

	DTOBaiduNotificationAndroid(BaiduNotificationAndroid baiduNotificationAndroid) {
		this.title = baiduNotificationAndroid.getTitle();
		this.description = baiduNotificationAndroid.getDescription();
		this.notificationBuilderId = baiduNotificationAndroid.getNotificationBuilderId();
		this.notificationBasicStyle = baiduNotificationAndroid.getNotificationBasicStyle();
		this.openType = baiduNotificationAndroid.getOpenType();
		this.url = baiduNotificationAndroid.getUrl();
		this.pkgContent = baiduNotificationAndroid.getPkgContent();
	}
}

@Getter
@JsonInclude(Include.NON_NULL)
class DTOBaiduNotificationIos {
	private final String alert;
	private final String sound;
	private final Integer badge;
	private final Integer deployStatus;

	DTOBaiduNotificationIos(BaiduNotificationIos baiduNotificationIos) {
		this.alert = baiduNotificationIos.getAlert();
		this.sound = baiduNotificationIos.getSound();
		this.badge = baiduNotificationIos.getBadge();
		this.deployStatus = baiduNotificationIos.getDeployStatus();
	}
}

@Getter
@JsonInclude(Include.NON_NULL)
class DTOBulkPushPayload {
	private final DTOPushPayload notification;
	private final List<DTOUserNotification> userNotifications;

	DTOBulkPushPayload(PushPayload notification, Collection<UserNotification> userNotifications) {
		this.notification = new DTOPushPayload(notification);
		this.userNotifications = Utils.safeMapList(userNotifications, DTOUserNotification::new);
	}
}

@Getter
@JsonInclude(Include.NON_NULL)
class DTOCapabilityUser {
	private final Integer badge;
	private final List<DTOCapabilityUserBadge> badges;
	private final List<String> formFactor;
	private final String user;

	DTOCapabilityUser(CapabilityUser capabilityUser) {
		this.badge = capabilityUser.getBadge();
		this.badges = Utils.safeMap(capabilityUser.getBadges(), badges -> badges.entrySet().stream()
				.map(e -> new DTOCapabilityUserBadge(e.getKey(), e.getValue())).collect(Collectors.toList()));
		this.formFactor = Utils.safeMapList(capabilityUser.getFormFactor(), Function.identity());
		this.user = capabilityUser.getUser();
	}
}

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@JsonInclude(Include.NON_NULL)
class DTOCapabilityUserBadge {
	private String key;
	private Integer value;
}

@Getter
@JsonInclude(Include.NON_NULL)
class DTOGcmNotification {

	private final String collapseKey;
	private final Boolean delayWhileIdle;
	private final String timeToLive;
	private final String restrictedPackageName;
	private final String title;
	private final String body;
	private final String icon;
	private final String sound;
	private final String tag;
	private final String color;
	private final String clickAction;
	private final String bodyLocKey;
	private final String bodyLocArgs;
	private final List<String> bodyLocArgsArray;
	private final String titleLocKey;
	private final String titleLocArgs;
	private final List<String> titleLocArgsArray;

	DTOGcmNotification(GcmNotification gcmNotification) {
		this.collapseKey = gcmNotification.getCollapseKey();
		this.delayWhileIdle = gcmNotification.getDelayWhileIdle();
		this.timeToLive = Utils.safeMap(gcmNotification.getTimeToLive(), DTOGcmNotification::ttlFormat);
		this.restrictedPackageName = gcmNotification.getRestrictedPackageName();
		this.title = gcmNotification.getTitle();
		this.body = gcmNotification.getBody();
		this.icon = gcmNotification.getIcon();
		this.sound = gcmNotification.getSound();
		this.tag = gcmNotification.getTag();
		this.color = gcmNotification.getColor();
		this.clickAction = gcmNotification.getClickAction();
		this.bodyLocKey = gcmNotification.getBodyLocKey();
		this.bodyLocArgs = gcmNotification.getBodyLocArgs();
		this.bodyLocArgsArray = gcmNotification.getBodyLocArgsArray();
		this.titleLocKey = gcmNotification.getTitleLocKey();
		this.titleLocArgs = gcmNotification.getTitleLocArgs();
		this.titleLocArgsArray = gcmNotification.getTitleLocArgsArray();
	}

	/**
	 * Formats the time-to-live according to FCM documentation.
	 *
	 * @param duration duration to be formatted
	 * @return formatted string or null, if param was null.
	 * @see <a target="_top" href=
	 * "https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages/#androidconfig">https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages/#androidconfig</a>
	 */
	static String ttlFormat(Duration duration) {
		if (duration == null) {
			return null;
		}
		long nanos = duration.toNanos();
		long seconds = nanos / (long) Math.pow(10, 9);
		long nano_rest = nanos % (seconds * (long) Math.pow(10, 9));

		StringBuilder sb = new StringBuilder();
		sb.append(seconds);
		if (nano_rest > 0) {
			sb.append(".").append(String.format("%09d", nano_rest));
		}
		sb.append("s");
		return sb.toString();
	}
}

@Getter
@Setter
@NoArgsConstructor
class DTOGetNotificationStatusResponse {
	private DTOStatusResponseStatus status;
	private DTONotificationStatusDetails statusDetails;
}

@Getter
@Setter
@NoArgsConstructor
class DTONotificationStatusDetails {
	private String notificationId;
	private String applicationId;
	private String notificationType;
	private String status;
	private String statusInfo;
	private String caller;
}

@Getter
@JsonInclude(Include.NON_NULL)
class DTOPushPayload {
	private final String alert;
	private final Integer badge;
	private final String sound;
	private final String priority;
	private final String data;
	private final Boolean sendAsSms;
	private final DTOApnsNotification apns;
	private final DTOGcmNotification gcm;
	private final DTOWnsNotification wns;
	private final DTOBaiduNotification baidu;
	private final DTOW3cNotification w3c;
	private final Map<String, String> custom;

	DTOPushPayload(PushPayload pushPayload) {
		this.alert = pushPayload.getAlert();
		this.badge = pushPayload.getBadge();
		this.sound = pushPayload.getSound();
		this.priority = pushPayload.getPriority();
		this.data = pushPayload.getData();
		this.sendAsSms = pushPayload.getSendAsSms();
		this.apns = Utils.safeMap(pushPayload.getApns(), DTOApnsNotification::new);
		this.gcm = Utils.safeMap(pushPayload.getGcm(), DTOGcmNotification::new);
		this.wns = Utils.safeMap(pushPayload.getWns(), DTOWnsNotification::new);
		this.baidu = Utils.safeMap(pushPayload.getBaidu(), DTOBaiduNotification::new);
		this.w3c = Utils.safeMap(pushPayload.getW3c(), DTOW3cNotification::new);
		this.custom = pushPayload.getCustom();
	}
}

@Getter
@Setter
@NoArgsConstructor
class DTOPushResponse implements PushResponse {
	private DTOStatusResponseStatus status;
	@JsonProperty("pushResults")
	private List<DTOPushResult> results;
}

@Getter
@Setter
@NoArgsConstructor
class DTOPushResult implements PushResult {
	private String target;
	private String notificationId;
	private Integer code;
	private String message;
}

@Getter
@Setter
@NoArgsConstructor
class DTONotificationStatusResponse implements NotificationStatusResponse {
	private DTOStatusResponseStatus status;
	private DTONotificationStatus statusDetails;
}

@Getter
@Setter
@NoArgsConstructor
class DTONotificationStatus implements NotificationStatus {
	private Status status;
	private String caller;
	private String notificationType;
}

@Getter
@JsonInclude(Include.NON_NULL)
class DTOPushToCapabilitiesPayload {
	private final List<DTOCapabilityUser> users;
	private final DTOPushPayload notification;

	DTOPushToCapabilitiesPayload(PushToCapabilitiesPayload pushToCapabilitiesPayload) {
		this.users = Utils.safeMapList(pushToCapabilitiesPayload.getCapabilityUsers(), DTOCapabilityUser::new);
		this.notification = Utils.safeMap(pushToCapabilitiesPayload.getNotification(), DTOPushPayload::new);
	}
}

@Getter
@JsonInclude(Include.NON_NULL)
class DTOPushToUsersPayload {
	private final List<String> users;
	private final DTOPushPayload notification;

	DTOPushToUsersPayload(List<String> users, PushPayload notification) {
		this.users = users;
		this.notification = new DTOPushPayload(notification);
	}
}

@Getter
@Setter
@NoArgsConstructor
class DTOStatusResponse {
	private DTOStatusResponseStatus status;
}

@Getter
@Setter
@NoArgsConstructor
class DTOStatusResponseStatus implements StatusResponseStatus {
	private Integer code;
	private String message;
	private Map<String, String> parameters;
}

@Getter
@JsonInclude(Include.NON_NULL)
class DTOUserNotification {
	private final String user;
	private final DTOPushPayload notification;

	DTOUserNotification(UserNotification userNotification) {
		this.user = userNotification.getUser();
		this.notification = Utils.safeMap(userNotification.getNotification(), DTOPushPayload::new);
	}
}

@Getter
@JsonInclude(Include.NON_NULL)
class DTOW3cNotification {
	private final byte[] blob;
	private final Long ttl;

	DTOW3cNotification(W3cNotification w3cNotification) {
		this.blob = w3cNotification.getBlob();
		this.ttl = Utils.safeMap(w3cNotification.getTtl(), Duration::getSeconds);
	}
}

@Getter
@JsonInclude(Include.NON_NULL)
class DTOWnsNotification {
	private final List<String> schema;
	private final String badge;
	private final DTOWnsNotificationCommands commands;
	private final DTOWnsNotificationAudio audio;
	private final List<DTOWnsNotificationImage> image;
	private final String version;
	private final String lang;
	private final String baseUri;
	private final String tileTemplate;
	private final String toastTemplate;
	private final String rawData;
	private final List<String> message;

	DTOWnsNotification(WnsNotification wnsNotification) {
		this.schema = wnsNotification.getSchema();
		this.badge = wnsNotification.getBadge();
		this.commands = Utils.safeMap(wnsNotification.getCommands(), DTOWnsNotificationCommands::new);
		this.audio = Utils.safeMap(wnsNotification.getAudio(), DTOWnsNotificationAudio::new);
		this.image = Utils.safeMapList(wnsNotification.getImage(), DTOWnsNotificationImage::new);
		this.version = wnsNotification.getVersion();
		this.lang = wnsNotification.getLang();
		this.baseUri = wnsNotification.getBaseUri();
		this.tileTemplate = wnsNotification.getTileTemplate();
		this.toastTemplate = wnsNotification.getToastTemplate();
		this.rawData = wnsNotification.getRawData();
		this.message = wnsNotification.getMessage();
	}
}

@Getter
@JsonInclude(Include.NON_NULL)
class DTOWnsNotificationAudio {
	private final Boolean loop;
	private final Boolean silent;
	private final String src;

	DTOWnsNotificationAudio(WnsNotificationAudio wnsNotificationAudio) {
		this.loop = wnsNotificationAudio.getLoop();
		this.silent = wnsNotificationAudio.getSilent();
		this.src = wnsNotificationAudio.getSrc();
	}
}

@Getter
@JsonInclude(Include.NON_NULL)
class DTOWnsNotificationCommands {
	private final String scenario;
	private final List<DTOWnsNotificationCommandsCommand> command;

	DTOWnsNotificationCommands(WnsNotificationCommands wnsNotificationCommands) {
		this.scenario = wnsNotificationCommands.getScenario();
		this.command = Utils.safeMapList(wnsNotificationCommands.getCommand(), DTOWnsNotificationCommandsCommand::new);
	}
}

@Getter
@JsonInclude(Include.NON_NULL)
class DTOWnsNotificationCommandsCommand {
	private final String id;
	private final String arguments;

	DTOWnsNotificationCommandsCommand(WnsNotificationCommandsCommand wnsNotificationCommandsCommand) {
		this.id = wnsNotificationCommandsCommand.getId();
		this.arguments = wnsNotificationCommandsCommand.getArguments();
	}
}

@Getter
@JsonInclude(Include.NON_NULL)
class DTOWnsNotificationImage {
	private final String alt;
	private final String src;
	private final Boolean addImageQuery;

	DTOWnsNotificationImage(WnsNotificationImage wnsNotificationImage) {
		this.alt = wnsNotificationImage.getAlt();
		this.src = wnsNotificationImage.getSrc();
		this.addImageQuery = wnsNotificationImage.getAddImageQuery();
	}
}

@Getter
@JsonInclude(Include.NON_NULL)
class DTOLocalizedPushPayload {
	private final DTOPushPayload notification;
	private final Map<String, DTOPushPayload> notifications;

	DTOLocalizedPushPayload(LocalizedPushPayload localizedPushPayload) {
		this.notification = Utils.safeMap(localizedPushPayload.getNotification(), DTOPushPayload::new);
		this.notifications =
				Utils.safeMapMap(localizedPushPayload.getNotifications(), Function.identity(), DTOPushPayload::new);
	}

	DTOLocalizedPushPayload(PushPayload pushPayload) {
		this.notification = new DTOPushPayload(pushPayload);
		this.notifications = null;
	}
}

@Getter
@JsonInclude(Include.NON_NULL)
class DTOLocalizedPushToUsersPayload {
	private final List<String> users;
	private final DTOLocalizedPushPayload notification;

	DTOLocalizedPushToUsersPayload(List<String> users, LocalizedPushPayload notification) {
		this.users = users;
		this.notification = new DTOLocalizedPushPayload(notification);
	}
}

@Getter @JsonInclude(Include.NON_NULL)
class DTOLocalizedPushToTopicPayload {
		private final List<String> users;
		private final List<String> topics;
		private final DTOLocalizedPushPayload notification;

	DTOLocalizedPushToTopicPayload(List<String> users, List<String> topics,LocalizedPushPayload notification ) {
		this.users = users;
		this.topics = topics;
		this.notification = new DTOLocalizedPushPayload(notification);
    }
}


@Getter
@JsonInclude(Include.NON_NULL)
class DTOLocalizedBulkPush {
	private final DTOLocalizedPushPayload notification;
	private final List<DTOLocalizedUserNotification> userNotifications;

	DTOLocalizedBulkPush(LocalizedPushPayload notification, Collection<LocalizedUserNotification> userNotifications) {
		this.notification = new DTOLocalizedPushPayload(notification);
		this.userNotifications = Utils.safeMapList(userNotifications, DTOLocalizedUserNotification::new);
	}
}

@Getter
@JsonInclude(Include.NON_NULL)
class DTOLocalizedPushToCapabilitiesPayload {
	private final Collection<DTOCapabilityUser> users;
	private final DTOLocalizedPushPayload notification;

	DTOLocalizedPushToCapabilitiesPayload(LocalizedPushToCapabilitiesPayload localizedPushToCapabilitiesPayload) {
		this.users = Utils.safeMapCollection(localizedPushToCapabilitiesPayload.getUsers(), DTOCapabilityUser::new,
				Collectors.toSet());
		this.notification =
				Utils.safeMap(localizedPushToCapabilitiesPayload.getNotification(), DTOLocalizedPushPayload::new);
	}
}

@Getter
@JsonInclude(Include.NON_NULL)
class DTOLocalizedUserNotification {
	private final String user;
	private final DTOLocalizedPushPayload notification;

	DTOLocalizedUserNotification(LocalizedUserNotification userNotification) {
		this.user = userNotification.getUser();
		this.notification = Utils.safeMap(userNotification.getNotification(), DTOLocalizedPushPayload::new);
	}
}

@Getter
@Setter
@NoArgsConstructor
class DTOPushRegistration implements PushRegistration {
	private String id;
	private String device;
	private String deviceModel;
	private FormFactor formFactor;
	private String pushGroup;
	private Provider pushProvider;
	private String userLocale;
	private String username;

	@Override
	public String getDeviceId() {
		return this.getDevice();
	}

	@Override
	public String getGroup() {
		return this.getPushGroup();
	}

	@Override
	public Provider getProvider() {
		return this.getPushProvider();
	}

	@Override
	public String getLocale() {
		return this.getLocale();
	}
}

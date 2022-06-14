---
hide:

- navigation

---

# Mobile Services Notification Backend API

## Useful Links

* [SAP Help: Administration](https://help.sap.com/doc/f53c64b93e5140918d676b927a3cd65b/Cloud/en-US/docs-en/guides/features/push/admin/config.html)
* [SAP Help: API Documentation](https://help.sap.com/doc/f53c64b93e5140918d676b927a3cd65b/Cloud/en-US/docs-en/guides/features/push/api/backend-api.html)
* [SAP Help: Native Provider Notification Handling](https://help.sap.com/doc/f53c64b93e5140918d676b927a3cd65b/Cloud/en-US/docs-en/guides/features/push/native-providers.html)

## Basic Example

``` java
MobileServicesSettings mobileServicesSettings = MobileServicesSettings
    .fromResource("mobileservices.json");
PushClient pushClient = new PushClientBuilder().build(mobileServicesSettings);

ApnsNotification apnsNotification = ApnsNotification.builder()
    .alertTitle("alert_title").alertBody("alert_body").build();
PushPayload pushPayload = PushPayload.builder().alert("alert")
    .apns(apnsNotification).build();

PushResponse pushResponse = pushClient.pushToUser("userId", pushPayload);
```

## Available Configuration Options

### Connect Timeout

You may configure the connection timeout of the underlying HTTP client. If not configured, defaults to the default of
the underlying client.

``` java
builder.withConnectTimeout(Duration.ofMillis(200))
```

### Read Timeout

You may configure the read timeout of the underlying HTTP client. If not configured, defaults to the default of the
underlying client.

``` java
builder.withReadTimeout(Duration.ofSeconds(2))
```

## PushClient Methods

### pushToApplication

This method allows to send a notification to all registered devices of a Mobile Application. It only requires the push
payload as a parameter.

### pushToDevice

This method sends a notification to a single registered device. You will need the user id and the device id to identify
the device.

### pushToUsers

This will send the same notification to a list of users. If a user has multiple devices registered, all of them will
receive the notification.

### pushToGroup

This will send the same notification to a specific group. The group membership of a user / device is controlled via the
device registration.

### bulkPush

Bulk push allows sending individual notifications to a list of users. The root notification is the "default" payload, if
there is no specific notification for a user configured.

### pushToCapability

Devices can register capabilities, by which they can be selected for notifications. This method will send the same
notification to all given users and their matching devices.

### getNotificationStatus

Allows your system to monitor sent notifications for success or failure. Your app may also confirm reception of the
message.

## Building a PushPayload

Building push payloads is supported by the builder pattern. You may create a basic push payload as follows:

```java
PushPayload pushPayload=PushPayload.builder().alert("My Alert").build();
```

Creating the specific APNS and FCM/GCM (and all other) payload objects follows the same structure and can be used in the
PushPayload builder:

```java
ApnsNotification apnsNotification=ApnsNotification.builder()
		.alertTitle("alert_title").alertBody("alert_body").build();
		PushPayload pushPayload=PushPayload.builder().apns(apnsNotification).build();
```

### Common Message Properties

The current REST API defines several common message properties, but mixes delivery options with message fields.

**Note**:The property `sound` will have different values for APNs and FCM.

| Property | Description                                       | APNs Mapping/Notes                                                    | FCM Mapping/Notes                                      | Others                   |
|:--------:|---------------------------------------------------|-----------------------------------------------------------------------|--------------------------------------------------------|--------------------------|
| `alert`  | Single message shown on the device                | `alert.title`                                                         | `alert`                                                | Supported by W3C and WNS |
| `badge`  | Number shown at the app icon on the device screen | Directly set by the notification manager when app is idle or inactive | Handled by notification callback                       | not supported by W3C     |
| `sound`  | Sound played back when notification is shown      | References sound file in '/Library/Sounds'                            | References sound file in app resource folder `res/raw` | not supported by W3C     |
|  `data`  | Dictionary (key/value map)                        | Handed over to notification callback, implemented by device           | Handled by notification callback                       |                          |

### Common Delivery Options

|  Property  | Description                                       | APNs Mapping/Notes          | FCM Mapping/Notes | Others |
|:----------:|---------------------------------------------------|-----------------------------|-------------------|--------|
| `priority` | Notification delivery priority `high` or `normal` | Translated to integer value | Used directly     |        |

## Error Handling

The client will throw a list of RuntimeExceptions in case of any unexpected response from the backend.

| Exception                     | Description | Mitigation |
|-------------------------------| --- | --- |
| `ClientException`             | Common root exception for all errors in the client. | n/a |
| `ClientUnauthorizedException` | The authorization to SAP Mobile Services failed. | Check your Mobile Services configuration. For example the configuration of service key scopes might be wrong. |
| `NoMessageSentException`      | No message has been sent. There may have been an error in the payload or the identifiers of recipients. | Check the response for more details on what went wrong. |
| `TrialLimitExceededException` | You are using a trial plan and exceeded the request limits for it. | Wait until you can send requests again. |

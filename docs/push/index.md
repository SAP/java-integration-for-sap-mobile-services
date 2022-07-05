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

### Tenant ID

You may configure a static tenant id. The id must be associated with a subscriber of your Mobile Services application.
If not configured, defaults to the provider tenant.

```java
builder.withTenantId("19f52077-c4fc-43b8-a8eb-4995779e1fa1")
```

Note, this option is incompatible with the [Tenant Supplier](#tenant-supplier) configuration.

### Tenant Supplier

You may configure a dynamic tenant supplier. The `get()` function is called for each request to determine the subscriber.
The function can return the tenant id wrapped in an `Optional` or an empty `Optional` to use the provider tenant.

The tenant id of a subscriber can be obtained during the `onSubscription` call of the [SaaS Provisioning Service](https://help.sap.com/docs/BTP/65de2977205c403bbc107264b8eccf4b/3971151ba22e4faa9b245943feecea54.html) or in the [Subscription Management Dashboard](https://help.sap.com/docs/BTP/65de2977205c403bbc107264b8eccf4b/434be695f9e946ccb4c28911dd1e16d0.html).

```java
builder.withTenantSupplier(() -> {
    // TODO: retrieve tenantId (e.g. from request context)
    String tenantId = "19f52077-c4fc-43b8-a8eb-4995779e1fa1";
    return Optional.of(tenantId);
})
```

Note, this option is incompatible with the [Tenant ID](#tenant-id) configuration.  

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

### pushToTopics

Devices can subscribe to topics to receive notifications based on that. This method sends the notification to all subscribers to any of the given topics.

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

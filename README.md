# Java integration for SAP Mobile Services

[![REUSE status](https://api.reuse.software/badge/github.com/SAP/java-integration-for-sap-mobile-services)](https://api.reuse.software/info/github.com/SAP/java-integration-for-sap-mobile-services)

## About this project

**Java integration for SAP Mobile Services** is a client library to integrate your application with the backend-facing APIs of SAP Mobile Services. As this project only started, it at the moment only provides the integration to the Notification Backend services, which allows you to send push notifications to your mobile devices. Other integrated APIs may be added to this library in the future.

## Requirements and Setup

<!--
The Java integration for SAP Mobile Services can simply be included as a Maven dependency:

```xml
<dependency>
    <groupId>//ENTER GROUP ID//</groupId>
    <artifactId>//ENTER ARTIFACT ID//</artifactId>
    <version>//ENTER LATEST BUILD VERSION//</version>
</dependency>
```
-->

Basic initialization of a PushClient:

```java
MobileServicesSettings mobileServicesSettings = MobileServicesSettings.fromResource("mobileservices.json");
PushClient pushClient = new PushClientBuilder().build(mobileServicesSettings);
```

**[Documentation](https://ideal-waddle-22e7094a.pages.github.io/main)**

## Support, Feedback, Contributing

This project is open to feature requests/suggestions, bug reports etc. via [GitHub issues](https://github.com/SAP/java-integration-for-sap-mobile-services/issues). Contribution and feedback are encouraged and always welcome. For more information about how to contribute, the project structure, as well as additional contribution information, see our [Contribution Guidelines](CONTRIBUTING.md).

## Code of Conduct

We as members, contributors, and leaders pledge to make participation in our community a harassment-free experience for everyone. By participating in this project, you agree to abide by its [Code of Conduct](CODE_OF_CONDUCT.md) at all times.

## Licensing

Copyright 2022 SAP SE or an SAP affiliate company and Java integration for SAP Mobile Services contributors. Please see our [LICENSE](LICENSE) for copyright and license information. Detailed information including third-party components and their licensing/copyright information is available [via the REUSE tool](https://api.reuse.software/info/github.com/SAP/java-integration-for-sap-mobile-services).

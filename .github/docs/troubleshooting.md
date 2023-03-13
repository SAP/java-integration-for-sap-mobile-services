# Troubleshooting Guide

This document collects known issues with the build and releases process and how to fix them.

## GitHub Actions

### Not Allowed Action / No Jobs Were Run

When adding a new or upgrading an existing action in any of the workflows, you might end up with the following error:

```<xyz> is not allowed to be used in SAP/java-integration-for-sap-mobile-services. Actions in this workflow must be: within a repository that belongs to your Enterprise account, created by GitHub, verified in the GitHub Marketplace, or matching the following:```

This is due to the project security settings.

We only allow to execute our own actions, verified 3rd party ones and specific ones.

In case you get this error, go to the [Actions permissions page](https://github.com/SAP/java-integration-for-sap-mobile-services/settings/actions) and your action with version to the list of `Allow specified actions and reusable workflows`:

Example:

```
jacobtomlinson/gha-find-replace@v3,
peter-evans/create-pull-request@v4,
```

### Invalid Credentials for Validation Broker

When you forgot to renew the mTLS credentials for the validation broker, you might get the following error in the system-tests workflow:

```
Run SAP/java-integration-for-sap-mobile-services@actions_prepare-mobile-services-app
Error: Request failed with status code 401
```

To resolve the issue, please follow [this guide](../../validation/mobile-service-broker/README.md#create--update-credentials) to renew the credentials.

### Error When Requesting Mobile Services App in System Tests

When the system-tests workflow fails to create a new Mobile Services app, you might get the following error:

```
Run SAP/java-integration-for-sap-mobile-services@actions_prepare-mobile-services-app
Error: Request failed with status code 500
```

In that case, it makes sense to inspect the log of the deployed [broker instance](../../validation/mobile-service-broker).

A common problem is, that the internal XSUAA mTLS credentials have expired.

To resolve the issue, please follow [this guide](../../validation/mobile-service-broker/README.md#create--update-credentials) to renew the credentials.

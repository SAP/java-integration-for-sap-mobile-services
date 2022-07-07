# Java Integration for SAP Mobile Services - Broker for Validation

This application allows to dynamically provision Mobile Services instances by calling an API endpoint.  
The endpoint is protected by XSUAA and requires a client-credential token.

The application needs to be deployed on a CF space which has entitlement and quote for Mobile Services.  
In addition, BTP platform user credentials have to be stored in a bound credstore instance.

Reason is to reduce the scope of the platform user to the testing pipelines, by only allowing to provision Mobile Services instances.

## Setup Guide

1. Run `mvn clean install`
2. Optionally, tweak the configuration environments (see [Configuration Parameters](#configuration-parameters]))
3. Deploy the application with `cf deploy --no-start`
   * Note, you'll need the [multiapps-cli-plugin](https://github.com/cloudfoundry/multiapps-cli-plugin)
4. Add the platform-user credentials
   1. Using the BTP Cockpit
      * follow [this guide](https://help.sap.com/docs/CREDENTIAL_STORE/601525c6e5604e4192451d5e7328fa3c/2a5423fc9ccb4ff3847cc6bd6c05b445.html) on how to manage credstore instances
      * Create a password called `platform-user` and provide the BTP platform-user credentials
        * namespace: `default`
        * username: the platform-user email
        * password: the platform-user password
   2. Use the credstore CF plugin
      * `cf credstore-create credstore-mobile-services-broker default password platform-user --value '<password>' --username '<username>'`
6. Deploy the application again and let it start: `cf deploy`
   * Note, once the credentials are stored in the credstore, you can don't need to use the `--no-start` option anymore.

## Configuration Parameters

| Name                               | Description                                                                                                          | Default                                 |
|------------------------------------|----------------------------------------------------------------------------------------------------------------------|-----------------------------------------|
| BROKER_SERVICE_NAME                | the CF service name of Mobile Services                                                                               | `mobile-services`                       |
| BROKER_PLAN_NAME                   | the CF service plan name of Mobile Services                                                                          | `standard`                              |
| BROKER_ORG_NAME                    | the CF org name where the service instances are created in                                                           | current org the broker is deployed in   |
| BROKER_SPACE_NAME                  | the CF space name where the service instances are created in                                                         | current space the broker is deployed in |
| BROKER_PREFIX                      | the prefix used for generating the mobile application names                                                          | `test-`                                 |
| BROKER_CREDENTIAL_ID               | the id of the platform-user credentials stored in the credential store                                               | `platform-user`                         |
| BROKER_MAX_INSTANCES               | the maximum number of concurrent mobile applications                                                                 | 15                                      |
| BROKER_NAME                        | the name of the Mobile Services service broker - optional: only required if service name and plan name are ambiguous |                                         |
| BROKER_MOBILE_APPLICATION_LIFETIME | the lifetime of the created mobile applications after which they get deleted automatically                           | `PT20M` (20 minutes)                    |

### Templates

Common app configurations can be registered as templates.  
The broker will hold a certain number of mobile applications for each template. When a new application is requested, the broker tries to serve it from the template instances to reduce response times.

Example:

```yaml
broker:
  templates:
    - keepNumber: 2
      features:
        - push
```

Keep two templates with push assigned in case it is required.

## API

OpenAPI document can be retrieved with `GET /v3/api-docs`

### Create new Mobile Application

`POST /api/broker/v1/apps?features=storage,push` to create a new mobile application with `storage` and `push` feature.

As response, you'll get the CF service-key.

### Delete a Mobile Application

`DELETE /api/broker/v1/apps/{appName}` where `appName` is the application name from the `sap.cloud.service` property.

If you don't remove the application manually, it will be deleted once the specified `lifetime` has been reached.

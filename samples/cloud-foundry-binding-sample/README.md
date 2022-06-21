# Java Integration for SAP Mobile Services - Cloud Foundry Binding Sample

This sample demonstrates how to configure the client in a Cloud Foundry application.  
The deployed application allows you to send notifications to all or to a specific user of your mobile app.

## Prerequisites

* A BTP account with Cloud Foundry runtime
  * You can create a free account [here](https://www.sap.com/products/business-technology-platform/trial.html)
* An SAP Mobile Services application with Push enabled on Cloud Foundry
* The Cloud Foundry CLI
  * You can download the CLI [here](https://docs.cloudfoundry.org/cf-cli/install-go-cli.html)
* Java 8 or higher with Maven 3.6 or higher

### Step 1 - Build the Project

Build the maven project:

```bash
mvn clean verify
```

### Step 2 - Locate the SAP Mobile Services Instance

Run `cf services` to get a list of service instances in your Cloud Foundry space.  
The output looks similar to the following example:

```bash
Getting services in org *** / space *** as ***...

name                     service           plan          bound apps   last operation     broker
com-sap-demo-app-xsuaa   xsuaa             application                create succeeded   ***
com-sap-demo-app         mobile-services   standard                   create succeeded   ***
[...]
```

Identify your SAP Mobile Services application and copy the name, in this example `com-sap-demo-app`.

### Step 3 - Deploy the Sample

Deploy the application to Cloud Foundry. You need to reference the instance name from the previous step:

```bash
cf push --var mobile-services-instance=${service-instance-name}
```

Once the push is completed, the application route is displayed:

```bash
Waiting for app to start...

name:              mobile-services-cf-sample
requested state:   started
routes:            mobile-services-cf-sample-happy-wildebeest-wa.cfapps.eu10.hana.ondemand.com
```

Copy the route of the application, in this example `mobile-services-cf-sample-happy-wildebeest-wa.cfapps.eu10.hana.ondemand.com`.

### Step 4 - Trigger a Push Notification

#### Send to All Users

To push a notification to all users, send a POST request to `https://${application-route}/api/v1/push/all`, where `${application-route}` is the application route from the previous step.

You can use your favorite HTTP client or run the following command:

```bash
curl -X POST https://${application-route}/api/v1/push/all
```

#### Send to Specific User

To push a notification to a specific user, send a POST request to `https://${application-route}/api/v1/push/users/${user-id}`, where `${application-route}` is the application route from the previous step and `${user-id}` is the user you want to push the notification to.

You can use your favorite HTTP client or run the following command:

```bash
curl -X POST https://${application-route}/api/v1/push/users/${user-id}
```

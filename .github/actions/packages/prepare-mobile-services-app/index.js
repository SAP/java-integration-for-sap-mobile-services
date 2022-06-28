const core = require("@actions/core");
const client = require('./client');
const fs = require('fs/promises');

async function run() {
    try {
        const brokerEndpoint = core.getInput("broker-endpoint");
        const xsuaaClientId =
            core.getInput("broker-xsuaa-client-id");
        const xsuaaUrl =
            core.getInput("broker-xsuaa-url");
        const xsuaaCert =
            core.getInput("broker-xsuaa-cert");
        const xsuaaKey =
            core.getInput("broker-xsuaa-key");
        const mobileFeatures = core.getInput("mobile-features");
        const parsedMobileFeatures = mobileFeatures.split(',').filter(f => f.length > 0);
        const serviceKeyPath = core.getInput('service-key-path');

        const mobileAppConfig = await client.createMobileApplication({
            brokerEndpoint: brokerEndpoint,
            xsuaaClientId: xsuaaClientId,
            xsuaaUrl: xsuaaUrl,
            xsuaaCert: xsuaaCert,
            xsuaaKey: xsuaaKey
        }, parsedMobileFeatures);

        await fs.writeFile('.mobile_app_id', mobileAppConfig['sap.cloud.service'], {
            encoding: 'utf-8'
        });

        const serviceKeyJsonString = JSON.stringify(mobileAppConfig)
        if (serviceKeyPath) {
            await fs.writeFile(serviceKeyPath, serviceKeyJsonString, {
                encoding: 'utf-8'
            });
        }

        core.setSecret('uaa-client-id', mobileAppConfig.uaa.clientid);
        core.setSecret('uaa-client-secret', mobileAppConfig.uaa.clientsecret);
        core.setSecret('service-key', serviceKeyJsonString);
    } catch (error) {
        core.setFailed(error.message);
    }
}

run();

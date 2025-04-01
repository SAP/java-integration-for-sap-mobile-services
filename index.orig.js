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
        const configurations = JSON.parse(core.getInput('configurations') || '[]');

        const clientConfig = {
            brokerEndpoint: brokerEndpoint,
            xsuaaClientId: xsuaaClientId,
            xsuaaUrl: xsuaaUrl,
            xsuaaCert: xsuaaCert,
            xsuaaKey: xsuaaKey
        };

        const mobileAppConfig = await client.createMobileApplication(clientConfig, parsedMobileFeatures);
        const appId = mobileAppConfig['sap.cloud.service'];
        await fs.writeFile('.mobile_app_id', appId, {
            encoding: 'utf-8'
        });

        const serviceKeyJsonString = JSON.stringify(mobileAppConfig)
        if (serviceKeyPath) {
            await fs.writeFile(serviceKeyPath, serviceKeyJsonString, {
                encoding: 'utf-8'
            });
        }

        for (const configOption of configurations) {
            const config = await client.createMobileServicesConfiguration(clientConfig, appId, {
                serviceName: configOption.serviceName,
                scopes: configOption.scopes,
                x509: configOption.x509 === true
            });
            const configJsonString = JSON.stringify(config);
            await fs.writeFile(configOption.configPath, configJsonString, {
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

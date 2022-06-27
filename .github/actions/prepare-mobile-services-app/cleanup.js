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
        const serviceKeyPath = core.getInput('service-key-path');

        if (serviceKeyPath) {
            try {
                await fs.rm(serviceKeyPath);
            } catch (e) {
                //ignore
            }
        }

        let appId;
        try {
            appId = await fs.readFile('.mobile_app_id', {
                encoding: 'utf-8'
            });
        } catch (e) {
            return;
        }

        await client.deleteMobileApplication({
            brokerEndpoint: brokerEndpoint,
            xsuaaClientId: xsuaaClientId,
            xsuaaUrl: xsuaaUrl,
            xsuaaCert: xsuaaCert,
            xsuaaKey: xsuaaKey
        }, appId);

        await fs.rm('.mobile_app_id');
    } catch (error) {
        core.setFailed(error.message);
    }
}

run();

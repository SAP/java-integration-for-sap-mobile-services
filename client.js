const got = require("got");
const xssec = require("@sap/xssec");


async function createMobileApplication(config, features) {
    const brokerEndpoint = config.brokerEndpoint;
    const xsuaaConfig = convertXsuaaConfig(config);

    const token = await fetchToken(xsuaaConfig);
    const response = await got.post(`${brokerEndpoint}/api/broker/v1/apps?features=${features.join(',')}`, {
        headers: {
            'authorization': `bearer ${token}`
        }
    }).json();

    return response;
}

async function createMobileServicesConfiguration(config, appId, serviceKeyRequest) {
    const brokerEndpoint = config.brokerEndpoint;
    const xsuaaConfig = convertXsuaaConfig(config);

    const token = await fetchToken(xsuaaConfig);
    const response = await got.post(`${brokerEndpoint}/api/broker/v1/apps/${appId}/configurations`, {
        json: serviceKeyRequest,
        headers: {
            'authorization': `bearer ${token}`
        }
    }).json();

    return response;
}

async function deleteMobileApplication(config, appId) {
    const brokerEndpoint = config.brokerEndpoint;
    const xsuaaConfig = convertXsuaaConfig(config);

    const token = await fetchToken(xsuaaConfig);
    const response = await got.delete(`${brokerEndpoint}/api/broker/v1/apps/${appId}`, {
        headers: {
            'authorization': `bearer ${token}`
        }
    }).json();

    return response.body;
}

function convertXsuaaConfig(config) {
    return {
        clientid: config.xsuaaClientId,
        url: config.xsuaaUrl,
        certurl: config.xsuaaUrl,
        certificate: formatPem(config.xsuaaCert),
        key: formatPem(config.xsuaaKey)
    }
}

function formatPem(pem) {
    const pemWithoutLines = pem.replace(/\n/g, "");
    let formattedCert = "";
    const pattern = /(-----.*?-----)(.*?)(-----.*?-----)/g;
    let matcher = null;

    while ((matcher = pattern.exec(pemWithoutLines))) {
        formattedCert += matcher[1] + "\n";

        const pemContent = matcher[2];
        for (let i = 0; i < pemContent.length; i += 64) {
            formattedCert += pemContent.substring(i, Math.min(i + 64, pemContent.length)) + "\n";
        }

        formattedCert += matcher[3] + "\n";
    }

    if (formattedCert.endsWith("\n")) {
        formattedCert = formattedCert.substring(0, formattedCert.length - 1);
    }

    return formattedCert;
}

async function fetchToken(config) {
    return new Promise((resolve, reject) => {
        xssec.requests.requestClientCredentialsToken(undefined, config, undefined, undefined, (error, token) => {
                if (error) {
                    reject(error);
                }
                if (!token) {
                    reject(
                        new Error("Failed to fetch token, no error details have been provided")
                    );
                } else {
                    resolve(token);
                }
            }
        );
    });
}

module.exports = {
    createMobileApplication: createMobileApplication,
    createMobileServicesConfiguration: createMobileServicesConfiguration,
    deleteMobileApplication: deleteMobileApplication
}
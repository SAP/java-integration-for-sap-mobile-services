const core = require('@actions/core');
const semver = require('semver-parser');
const moment = require('moment');

try {

    const currentVersion = core.getInput('current-version');
    const type = core.getInput('type');
    const isDev = (core.getInput('is-dev') === 'true');

    const parsedVersion = semver.parseSemVer(currentVersion, true);
    const currentBranch = toBranch(parsedVersion);
    let suffix = isDev ? '-dev' : '';
    let targetVersion;
    let targetBranch;
    let targetTag;

    if (type === 'major') {
        targetVersion = `${parsedVersion.major + 1}.0.0${suffix}`;
    } else if (type === 'minor') {
        targetVersion = `${parsedVersion.major}.${parsedVersion.minor + 1}.0${suffix}`;
    } else if (type === 'patch') {
        targetVersion = `${parsedVersion.major}.${parsedVersion.minor}.${parsedVersion.patch + 1}${suffix}`;
    } else if (type === 'release') {
        targetVersion = `${parsedVersion.major}.${parsedVersion.minor}.${parsedVersion.patch}${suffix}`;
    } else if (type === 'run') {
        const commitId = core.getInput('commit-id');
        if (commitId) {
            throw new Error("Missing commit-id");
        }
        const dateString = moment().format('YYYYMMDDHHmmss');
        targetVersion = `${parsedVersion.major}.${parsedVersion.minor}.${parsedVersion.patch}-${dateString}-${commitId}`;
    }

    targetBranch = toBranch(semver.parseSemVer(targetVersion, true));
    targetTag = toTag(semver.parseSemVer(targetVersion, true));

    core.setOutput("current-version", currentVersion);
    core.setOutput("current-branch", currentBranch);
    core.setOutput("target-version", targetVersion);
    core.setOutput("target-branch", targetBranch);
    core.setOutput("target-tag", targetTag);
} catch (error) {
    core.setFailed(error.message);
}

function toBranch(version) {
    return `rel/${version.major}.${version.minor}.x`;
}

function toTag(version) {
    return `v${version.major}.${version.minor}.${version.patch}`;
}
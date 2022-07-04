const core = require('@actions/core');
const github = require('@actions/github');
const semver = require('semver');

const REF_TAG_PREFIX = 'refs/tags/';
const REF_MAIN_BRANCH = 'refs/heads/main';
const TAG_VERSION_PREFIX = 'v';

async function run() {
  try {
    const token = core.getInput('github-token');
    const refName = core.getInput('ref');

    let refVersion = null;
    if (refName.startsWith(REF_TAG_PREFIX)) {
      refVersion = tryParseSemVer(refName.substring(REF_TAG_PREFIX.length));
    }

    const octoKit = github.getOctokit(token);
    const latestTagVersion = await getLatestTagVersion(octoKit);
    
    if (latestTagVersion) {
      const isLatest = refVersion !== null && semver.compare(refVersion, latestTagVersion, true) >= 0;
      core.setOutput('is-latest', isLatest);
    } else if (refName === REF_MAIN_BRANCH) {
      core.setOutput('is-latest', true);
    } else {
      core.setOutput('is-latest', false);
    }
  } catch (error) {
    core.setFailed(error.message);
  }
}

async function getLatestTagVersion(octoKit) {
  const tagIterator = getTagIterator(octoKit);
  let latestVersion = null;

  for await (const { data: tags } of tagIterator) {
    for (const tag of tags) {
      const version = tryParseSemVer(tag.name);
      if (version != null && (latestVersion == null || semver.compare(version, latestVersion, true) >= 0)) {
        latestVersion = version;
      }
    }
  }

  return latestVersion;
}

function getTagIterator(octoKit) {
  return octoKit.paginate.iterator(octoKit.rest.repos.listTags, {
    owner: github.context.repo.owner,
    repo: github.context.repo.repo,
    per_page: 100,
  });
}

function tryParseSemVer(tagName) {
  if (!tagName.startsWith(TAG_VERSION_PREFIX)) {
    return null;
  }
  const version = tagName.substring(TAG_VERSION_PREFIX.length);
  return semver.parse(version);
}

run();

const core = require('@actions/core');
const github = require('@actions/github');
const semver = require('semver');

const REL_BRANCH_PREFIX = 'rel/';

async function run() {
  try {
    const token = core.getInput('github-token');
    const branchName = core.getInput('branch');
    const branchVersion = tryParseSemVer(branchName);

    const octoKit = github.getOctokit(token);

    const branches = await listBranches(octoKit);
    const latestRelease = branches
        .map((branch) => branch.name)
        .map(tryParseSemVer)
        .filter((version) => version !== null)
        .sort((a, b) => semver.compare(b,a, true))[0] || null;

    const isLatest = latestRelease == null || branchVersion != null && semver.compare(branchVersion, latestRelease) >= 0;
    core.setOutput('is-latest', isLatest);
  } catch (error) {
    core.setFailed(error.message);
  }
}

async function listBranches(octoKit, page = 1) {
  const { data: branches } = await octoKit.rest.repos.listBranches({
    owner: github.context.repo.owner,
    repo: github.context.repo.repo,
    per_page: 100,
    page: page,
  });

  if (branches.length > 0) {
    branches.push(...(await listBranches(octoKit, page + 1)));
  }
  return branches;
}

function tryParseSemVer(branchName) {
  if (!branchName.startsWith(REL_BRANCH_PREFIX)) {
    return null;
  }
  const version = branchName
    .substring(REL_BRANCH_PREFIX.length)
    .replace(/x/g, '0'); //transform 1.2.x => 1.2.0
  return semver.parse(version);
}

run();

const core = require('@actions/core');
const github = require('@actions/github');

async function run() {
    try {
        const token = core.getInput('github-token');
        const targetBranch = core.getInput('release-branch');
        const currentBranch = core.getInput('current-branch');
        const releaseVersion = core.getInput('release-version');

        const octoKit = github.getOctokit(token);

        const branchExists = await doesBranchExist(octoKit, targetBranch);
        if (branchExists) {
            await createPr(octoKit, currentBranch, targetBranch, releaseVersion);
        } else {
            const branch = await getBranch(octoKit, currentBranch);
            await createBranch(octoKit, targetBranch, branch.commit.sha);
            if (branch.protected === false) {
                await deleteBranch(octoKit, currentBranch);
            }
        }
    } catch (error) {
        core.setFailed(error.message);
    }
}

async function doesBranchExist(octoKit, name) {
    try {
        await octoKit.rest.repos.getBranch({
            owner: github.context.repo.owner,
            repo: github.context.repo.repo,
            branch: name
        });
        return true;
    } catch (e) {
        if (e.name === 'HttpError' && e.status === 404) {
            return false;
        } else {
            throw e;
        }
    }
}

async function getBranch(octoKit, name) {
    const {data: branch} = await octoKit.rest.repos.getBranch({
        owner: github.context.repo.owner,
        repo: github.context.repo.repo,
        branch: name
    });
    return branch;
}

async function createPr(octoKit, headBranch, baseBranch, releaseVersion) {
    const title = `Update ${baseBranch} for ${releaseVersion || 'upcoming'} release`
    await octoKit.rest.pulls.create({
        owner: github.context.repo.owner,
        repo: github.context.repo.repo,
        head: headBranch,
        base: baseBranch,
        title: title
    });
}

async function createBranch(octoKit, name, commitSha) {
    await octoKit.rest.git.createRef({
        owner: github.context.repo.owner,
        repo: github.context.repo.repo,
        ref: `refs/heads/${name}`,
        sha: commitSha
    });
}

async function deleteBranch(octoKit, name) {
    await octoKit.rest.repos.deleteBranch({
        owner: github.context.repo.owner,
        repo: github.context.repo.repo,
        branch: name
    });
}

run();

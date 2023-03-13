# Build Pipelines

This project is using [GitHub Actions](https://docs.github.com/en/actions) to build, validate and release the library.

## Important Links

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Our Workflows](https://github.com/SAP/java-integration-for-sap-mobile-services/tree/main/.github/workflows)
- [Our Actions](https://github.com/SAP/java-integration-for-sap-mobile-services/tree/main/.github/actions)
- [Our Actions Dashboard](https://github.com/SAP/java-integration-for-sap-mobile-services/actions)

## Workflows

[Workflows](https://docs.github.com/en/actions/using-workflows/about-workflows) describe a pipeline-job consisting of steps to accomplish a certain task.  
Depending on the type, they can be triggered automatically (e.g. for a pull-request, on push, ...) or manually.

### Build

The [Build workflow](../workflows/build.yaml) executes the maven build and runs the system-tests.

It's triggered when pushing to the `main` branch, manually or when during a release (triggered by the [Perform Release workflow](#perform-release))

In order to execute the system-tests, the [Mobile Services Broker for Validation](../../validation/mobile-service-broker) is called to create a new Mobile Services application and retrieve the credentials.  
Therefore, make sure to properly set it up, provide the required credentials and rotate them regularly as described in [this guide](../../validation/mobile-service-broker).

### Bump Version

The [Bump Version workflow](../workflows/bump-version.yaml) create a new pull-request to bump the version to the next major, minor or path release.  

It's called during/after each release, in order to prepare the `main` branch but can also be called manually.

Due to restrictions in GitHub Actions, the PR must either be merged manually or approved for auto-merging. Pushing directly to the `main` branch isn't possible, because it's protected.

### CodeQL Analysis

The [CodeQL Analysis workflow](../workflows/codeql-analysis.yml) runs the CodeQL code scans.

It's executed when pushing to the `main` or any of the `patch-release (rel/*)` branches, is part of the required PR voters and is also executed once a week.

The job has been configured automatically and must not be modified.

### Perform Release

The [perform-release workflow](../workflows/perform-release.yaml) performs the actual release.

It calls different workflows in order to orchestrate the release:

- [Build](#build) in order to build and validate the project
- [Release Documentation](#release-documentation) in order to build and publish the documentation (mkdocs + java doc)

Once done, it performs the upload to Maven Central. Before that, it asks for approval from any of the admins.

This workflow can't be triggered manually, as it runs from the created git tag. Instead, call the [Trigger Release workflow](#trigger-release).

### PR Voter

The [PR Voter workflow](../workflows/pr-voter.yaml) is a required voter for any pull-request to the `main` branch.

It tries to rebase the pull-request from the `main` branch and executed a maven build and documentation build.

### Prepare Patch Release

The [Prepare Patch Release workflow](../workflows/prepare-patch-release.yaml) is used to create a temporary `patch-release (rel/*)` branch.

It's triggered based on a previous `release tag` in order to create a new branch from it and bumping the version to the next patch level.

After applying the required changes to that branch, the release procedure can be triggered from that branch.

More details about how to do a patch release can be found in the [How to Release guide](./how-to-release.md#create-a-patch-release).

### Release Action

The [Release Action workflow](../workflows/release-action.yaml) is used to release a single [Action](#actions).

As this happens automatically, it can't be triggered manually and shouldn't be modified unless broken.

### Release Actions

The [Release Actions workflow](../workflows/release-actions.yaml) is used to release all actions.

It's called whenever any of our [Actions](#actions) is modified.

### Release Documentation

The [Release Documentation workflow](../workflows/release-documentation.yaml) is used to build and release the mkdocs and java doc.

Depending on the context, it publishes the results to `gh-pages`:

- `main` branch: push to the `main` dir ([/main](https://sap.github.io/java-integration-for-sap-mobile-services/main/) + [/main/javadoc](https://sap.github.io/java-integration-for-sap-mobile-services/main/javadoc/))
- any release tag: push to the `${version}` dir (e.g. [/v0.4.0](https://sap.github.io/java-integration-for-sap-mobile-services/v0.4.0/) + [/v0.4.0/javadoc](https://sap.github.io/java-integration-for-sap-mobile-services/v0.4.0/javadoc/)) to archive existing release docs
- for the latest release: push to the `current` dir ([/current](https://sap.github.io/java-integration-for-sap-mobile-services/current/) + [/current/javadoc](https://sap.github.io/java-integration-for-sap-mobile-services/current/javadoc/)) to always link to the current release docs

## Actions

[Actions](https://docs.github.com/en/actions/creating-actions/about-custom-actions) are reusable components that can be triggered from a workflow.

They can define `input` parameters and `output` values and typically perform a specific task in order to

- reuse functionality across workflows
- make the workflow more readable by getting rid of `shell` statements

They can either be written as `Docker` image or as `JavaScript` actions.

We automatically release any action when pushing to the `main` branch as git tag (e.g. [actions_is-latest-release](https://github.com/SAP/java-integration-for-sap-mobile-services/tree/actions_is-latest-release/)) by the [Release Actions workflow](#release-actions).

An action can be called from our workflows, by referencing our own GH repo and the correct tag:

```yaml
- name: Bump Version
  id: bump-version
  uses: SAP/java-integration-for-sap-mobile-services@actions_bump-version
  with:
    current-version: ${{ env.artifactVersion }}
    type: ${{ inputs.type }}
    is-dev: true
```
(see [bump-version.yaml#L40-L46](https://github.com/SAP/java-integration-for-sap-mobile-services/blob/539464ff24e153a4d299583edddf5d9232e1c807/.github/workflows/bump-version.yaml#L40-L46))

### Build MkDocs

The [Build MkDocs action](../actions/packages/build-mkdocs/) is a `Docker` based action to build our documentation with mkdocs.

It's reusing the `squidfunk/mkdocs-material` image and simply modifies the entrypoint to work with the GitHub workspace directory by default.

### Bump Version

The [Bump Version action](../actions/packages/bump-version) is a `JavaScript` based action to generate the next major, minor or patch version of a provided version string.

It neither extracts the current maven version nor does it update it. Is simply generates the next version:

#### Input

| Name            | Description                                                      | Example / Allowed Values                 | Required             |
|-----------------|------------------------------------------------------------------|------------------------------------------|----------------------|
| current-version | the input version string                                         | 0.1.0                                    | yes                  |
| type            | the type of bump/next version to generate                        | major, minor, patch, run, release        | yes                  |
| commit-id       | the current commit id                                            | 539464ff24e153a4d299583edddf5d9232e1c807 | only for type=run    |
| is-dev          | whether the target version is a dev version or not (true, false) | true, false                              | no (default = false) |

For the type, the following values are allows:

- `major`: 0.5.1 -> 1.0.0 or  1.0.0 -> 2.0.0
- `minor`: 0.5.1 -> 0.6.0 or 1.0.0 -> 1.1.0
- `patch`: 0.5.1 -> 0.5.2 or 1.0.0 -> 1.0.1
- `run`: 0.5.1 -> 0.5.1-`${timestamp}`-`${commit}`
- `release`: 0.5.1-dev -> 0.5.1 or 1.0.0-dev -> 1.0.0

#### Output

| Name            | Description                                                      | Example   |
|-----------------|------------------------------------------------------------------|-----------|
| current-version | the same version as the version input                            | 0.1.0     |
| current-branch  | the current version release branch                               | rel/0.1.x |
| target-version  | the generated version based on input, type, commit-id and is-dev | 0.2.0     |
| target-branch   | the target version release branch                                | rel/0.2.x |
| target-tag      | the target version release tag                                   | v0.2.0    |

### Is Latest Release

The [Is Latest Release action](../actions/packages/is-latest-release) is a `JavaScript` based action to tell if a certain git ref is belonging to the latest release.

Is queries the GitHub API to fetch all existing release to validate if the specified ref is the latest version or belongs to a previous release.

This is used to prevent the documentation release of a patch release to overwrite the latest docs in gh-pages.

#### Input

| Name         | Description                                            | Example / Allowed Values | Required |
|--------------|--------------------------------------------------------|--------------------------|----------|
| github-token | the GH API token (provided by the GH actions workflow) | n/a                      | yes      |
| ref          | the current ref name                                   | refs/heads/main          | yes      |

#### Output

| Name      | Description                                               | Example |
|-----------|-----------------------------------------------------------|---------|
| is-latest | the flag whether the current ref is the latest one or not | true    |

### Prepare Mobile Services App

The [Prepare Mobile Services App action](../actions/packages/prepare-mobile-services-app) is a `JavaScript` based action to create a new Mobile Services application and provide the credentials.

It requires a deployed [Mobile Services Broker for Validation](../../validation/mobile-service-broker) instance with the configured credentials.

The action calls the broker to create a new application, optionally create service-keys and provides the credentials as JSON output.

After the calling workflow is exited, the application is removed automatically.

#### Input

| Name                   | Description                                                     | Example / Allowed Values                                                                     | Required |
|------------------------|-----------------------------------------------------------------|----------------------------------------------------------------------------------------------|----------|
| broker-endpoint        | the broker endpoint URL                                         | n/a                                                                                          | yes      |
| broker-xsuaa-client-id | the broker XSUAA client-id                                      | n/a                                                                                          | yes      |
| broker-xsuaa-url       | the broker XSUAA cert URL                                       | https://<subdomain>.authentication.cert.eu10.hana.ondemand.com                               | yes      |
| broker-xsuaa-cert      | the broker XSUAA mTLS certificate                               | n/a                                                                                          | yes      |
| broker-xsuaa-key       | the broker XSUAA mTLS key                                       | n/a                                                                                          | yes      |
| mobile-features        | the comma-separated list of features assigned to the mobile app | storage,push                                                                                 | no       |
| service-key-path       | the path to write the service-key to                            | ${{ github.workspace }}/validation/system-tests/service-key.json                             | no       |
| configurations         | additional mobile services configuration files                  | {"serviceName": "push", "x509": true, "scopes": ["push_all"], "configPath": "./config.json"} | no       |

#### Output

| Name              | Description                             | Example |
|-------------------|-----------------------------------------|---------|
| service-key       | the service-key document as JSON        | n/a     |
| uaa-client-id     | the UAA client-id of the mobile app     | n/a     |
| uaa-client-secret | the UAA client-secret of the mobile app | n/a     |

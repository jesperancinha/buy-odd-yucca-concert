# decK: Declarative configuration for Kong

decK provides declarative configuration and drift detection for Kong.

[![Build Status](https://github.com/kong/deck/workflows/CI%20Test/badge.svg)](https://github.com/kong/deck/actions?query=branch%3Amain+event%3Apush)
[![codecov](https://codecov.io/gh/Kong/deck/branch/main/graph/badge.svg?token=m9WNK9rFEG)](https://codecov.io/gh/Kong/deck)
[![Go Report Card](https://goreportcard.com/badge/github.com/kong/deck)](https://goreportcard.com/report/github.com/kong/deck)

[![asciicast](https://asciinema.org/a/238318.svg)](https://asciinema.org/a/238318)

## Table of Content

-   [**Features**](#features)
-   [**Compatibility**](#compatibility)
-   [**Installation**](#installation)
-   [**Documentation**](#documentation)
-   [**Stale issue and pull request policy**](#stale-issue-and-pull-request-policy)
-   [**License**](#license)

## Features

- **Export**  
  Existing Kong configuration to a YAML configuration file
  This can be used to backup Kong's configuration.
- **Import**  
  Kong's database can be populated using the exported or a hand written config
  file.
- **Diff and sync capabilities**  
  decK can diff the configuration in the config file and
  the configuration in Kong's DB and then sync it as well.
  This can be used to detect config drifts or manual interventions.
- **Reverse sync**  
  decK supports a sync the other way as well, meaning if an
  entity is created in Kong and doesn't add it to the config file,
  decK will detect the change.
- **Validation**  
  decK can validate a YAML file that you backup or modify to catch errors
  early on.
- **Reset**  
  This can be used to drops all entities in Kong's DB.
- **Parallel operations**  
  All Admin API calls to Kong are executed in parallel using multiple
  threads to speed up the sync process.
- **Authentication with Kong**
  Custom HTTP headers can be injected in requests to Kong's Admin API
  for authentication/authorization purposes.
- **Manage Kong's config with multiple config file**  
  Split your Kong's configuration into multiple logical files based on a shared
  set of tags amongst entities.
- **Designed to automate configuration management**  
  decK is designed to be part of your CI pipeline and can be used to not only
  push configuration to Kong but also detect drifts in configuration.

## Compatibility

decK is compatible with Kong Gateway >= 1.x and Kong Enterprise >= 0.35.

## Installation

### macOS

If you are on macOS, install decK using brew:

```shell
$ brew tap kong/deck
$ brew install deck
```

### Linux

If you are Linux, you can either use the Debian or RPM archive from
the GitHub [release page](https://github.com/kong/deck/releases)
or install by downloading the binary:

```shell
$ curl -sL https://github.com/kong/deck/releases/download/v1.8.1/deck_1.8.1_linux_amd64.tar.gz -o deck.tar.gz
$ tar -xf deck.tar.gz -C /tmp
$ sudo cp /tmp/deck /usr/local/bin/
```

### Windows

If you are on Windows, you can download the binary from the GitHub
[release page](https://github.com/kong/deck/releases) or via PowerShell:

```shell
$ curl -sL https://github.com/kong/deck/releases/download/v1.8.1/deck_1.8.1_windows_amd64.tar.gz -o deck.tar.gz
$ tar -xzvf deck.tar.gz
```

### Docker image

Docker image is hosted on [Docker Hub](https://hub.docker.com/r/kong/deck).

You can get the image with the command:

```
docker pull kong/deck
```

## Documentation

You can use `--help` flag once you've decK installed on your system
to get help in the terminal itself.

The project's documentation is hosted at
[https://docs.konghq.com/deck/overview](https://docs.konghq.com/deck/overview).

## Changelog

Changelog can be found in the [CHANGELOG.md](CHANGELOG.md) file.

## Stale issue and pull request policy

To ensure our backlog is organized and up to date, we will close issues and
pull requests that have been inactive awaiting a community response for over 2
weeks. If you wish to reopen a closed issue or PR to continue work, please
leave a comment asking a team member to do so.

## About me 👨🏽‍💻🚀🏳️‍🌈

[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/JEOrgLogo-20.png "João Esperancinha Homepage")](http://joaofilipesabinoesperancinha.nl)
[![GitHub followers](https://img.shields.io/github/followers/jesperancinha.svg?label=Jesperancinha&style=social "GitHub")](https://github.com/jesperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/mastodon-20.png "Mastodon")](https://masto.ai/@jesperancinha)
[![Twitter Follow](https://img.shields.io/twitter/follow/joaofse?label=João%20Esperancinha&style=social "Twitter")](https://twitter.com/joaofse)
| [Sessionize](https://sessionize.com/joao-esperancinha/)
| [Spotify](https://open.spotify.com/user/jlnozkcomrxgsaip7yvffpqqm?si=b54b89eae8894960)
| [Medium](https://medium.com/@jofisaes)
| [YouTube](https://www.youtube.com/@joaoesperancinha/featured)
| [Instagram](https://www.instagram.com/joaofisaes/)
| [Buy me a coffee](https://www.buymeacoffee.com/jesperancinha)
| [Credly Badges](https://www.credly.com/users/joao-esperancinha)
| [Google Apps](https://play.google.com/store/apps/developer?id=Joao+Filipe+Sabino+Esperancinha)
| [Sonatype Search Repos](https://search.maven.org/search?q=org.jesperancinha)
| [Docker Images](https://hub.docker.com/u/jesperancinha)
| [Stack Overflow Profile](https://stackoverflow.com/users/3702839/joao-esperancinha)
| [Reddit](https://www.reddit.com/user/jesperancinha/)
| [Dev.TO](https://dev.to/jofisaes)
| [Hackernoon](https://hackernoon.com/@jesperancinha)
| [Code Project](https://www.codeproject.com/Members/jesperancinha)
| [BitBucket](https://bitbucket.org/jesperancinha)
| [GitLab](https://gitlab.com/jesperancinha)
| [Coursera](https://www.coursera.org/user/da3ff90299fa9297e283ee8e65364ffb)
| [FreeCodeCamp](https://www.freecodecamp.org/jofisaes)
| [HackerRank](https://www.hackerrank.com/jofisaes)
| [LeetCode](https://leetcode.com/jofisaes)
| [Codebyte](https://coderbyte.com/profile/jesperancinha)
| [CodeWars](https://www.codewars.com/users/jesperancinha)
| [Code Pen](https://codepen.io/jesperancinha)
| [Hacker Earth](https://www.hackerearth.com/@jofisaes)
| [Khan Academy](https://www.khanacademy.org/profile/jofisaes)
| [Hacker News](https://news.ycombinator.com/user?id=jesperancinha)
| [InfoQ](https://www.infoq.com/profile/Joao-Esperancinha.2/)
| [LinkedIn](https://www.linkedin.com/in/joaoesperancinha/)
| [Xing](https://www.xing.com/profile/Joao_Esperancinha/cv)
| [Tumblr](https://jofisaes.tumblr.com/)
| [Pinterest](https://nl.pinterest.com/jesperancinha/)
| [Quora](https://nl.quora.com/profile/Jo%C3%A3o-Esperancinha)
| [VMware Spring Professional 2021](https://www.credly.com/badges/762fa7a4-9cf4-417d-bd29-7e072d74cdb7)
| [Oracle Certified Professional, Java SE 11 Programmer](https://www.credly.com/badges/87609d8e-27c5-45c9-9e42-60a5e9283280)
| [Oracle Certified Professional, JEE7 Developer](https://www.credly.com/badges/27a14e06-f591-4105-91ca-8c3215ef39a2)
| [IBM Cybersecurity Analyst Professional](https://www.credly.com/badges/ad1f4abe-3dfa-4a8c-b3c7-bae4669ad8ce)
| [Certified Advanced JavaScript Developer](https://cancanit.com/certified/1462/)
| [Certified Neo4j Professional](https://graphacademy.neo4j.com/certificates/c279afd7c3988bd727f8b3acb44b87f7504f940aac952495ff827dbfcac024fb.pdf)
| [Deep Learning](https://www.credly.com/badges/8d27e38c-869d-4815-8df3-13762c642d64)
| [![Generic badge](https://img.shields.io/static/v1.svg?label=GitHub&message=JEsperancinhaOrg&color=yellow "jesperancinha.org dependencies")](https://github.com/JEsperancinhaOrg)
[![Generic badge](https://img.shields.io/static/v1.svg?label=All%20Badges&message=Badges&color=red "All badges")](https://joaofilipesabinoesperancinha.nl/badges)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Status&message=Project%20Status&color=red "Project statuses")](https://github.com/jesperancinha/project-signer/blob/master/project-signer-quality/Build.md)

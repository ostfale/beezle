package de.ostfale.beezle.entity.repo

class Repo {

    final String repoName
    RepoStatus repoStatus = RepoStatus.REMOTE

    Repo(String repoName, RepoStatus status) {
        this.repoName = repoName
        this.repoStatus = status
    }
}

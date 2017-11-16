package de.ostfale.beezle.entity.repo

class Repo {

    final String repoName
    RepoStatus repoStatus = RepoStatus.NEW

    Repo(String repoName) {
        this.repoName = repoName
    }
}

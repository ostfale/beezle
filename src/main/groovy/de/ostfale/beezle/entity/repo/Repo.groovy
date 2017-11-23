package de.ostfale.beezle.entity.repo

class Repo {

    String repoName
    RepoStatus repoStatus = RepoStatus.ROOT

    Repo(String repoName, RepoStatus status) {
        this.repoName = repoName
        this.repoStatus = status
    }


    @Override
    String toString() {
        return repoName
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Repo repo = (Repo) o

        if (repoName != repo.repoName) return false

        return true
    }

    int hashCode() {
        return repoName.hashCode()
    }
}

package de.ostfale.beezle.entity.repo

class Repo {

    String repoName, repoPath
    RepoStatus repoStatus = RepoStatus.ROOT

    Repo(String repoName, RepoStatus status) {
        this.repoName = repoName
        this.repoStatus = status
    }


    @Override
    String toString() {
        return repoName
    }

    String getReadme() {
        String readme = ""
        if (repoPath) {
            File repoFile = new File(repoPath)
            if (repoFile.exists()) {
                repoFile.eachFile { File file ->
                    if (file.getName().toLowerCase().startsWith("readme")) {
                        readme = file.text
                    }
                }
            }
        }
        return readme
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

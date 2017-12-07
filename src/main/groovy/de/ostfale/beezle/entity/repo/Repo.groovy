package de.ostfale.beezle.entity.repo

import de.ostfale.beezle.control.repo.GitService

class Repo {

    String repoName, repoPath
    RepoStatus repoStatus = RepoStatus.ROOT

    Repo(String repoName, RepoStatus status) {
        this.repoName = repoName
        this.repoStatus = status
    }


    @Override
    String toString() {
        int changedFiles = getNoOfChangedFiles()
        if (changedFiles == 0) {
            return repoName
        }
        return "($changedFiles} $repoName"
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

    int getNoOfChangedFiles() {
        if (repoPath) {
            return GitService.getChangedFiles(new File(repoPath))
        }
        return 0
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

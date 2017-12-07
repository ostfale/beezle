package de.ostfale.beezle.control.repo

import groovy.util.logging.Slf4j
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.Status

/**
 * Implements some convenience functions to do git actions
 * Created by usauerbrei on 07.12.2017
 */
@Slf4j
class GitService {

    static int getChangedFiles(File repoDir) {
        Set<String> uncommittedChanges = new HashSet<>();
        Git git = Git.open(repoDir)
        Status status = git.status().call()
        uncommittedChanges.addAll(status.getChanged())
        uncommittedChanges.addAll(status.getAdded())
        uncommittedChanges.addAll(status.getUntracked())
        uncommittedChanges.addAll(status.getModified())
        uncommittedChanges.addAll(status.getRemoved())
        git.close()
        return uncommittedChanges.size()
    }
}

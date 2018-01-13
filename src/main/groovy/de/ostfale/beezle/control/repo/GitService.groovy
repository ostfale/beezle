package de.ostfale.beezle.control.repo

import de.ostfale.beezle.control.ApplService
import de.ostfale.beezle.control.PropertyService
import de.ostfale.beezle.control.UserProperties
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

    static Optional<String> getSSHPassword(Optional<File> propFile = ApplService.getPropertyFile()) {
        if (propFile.isPresent()) {
            String encryptedPassword = PropertyService.instance.getProperty(UserProperties.SSH_PASSWORD.key, propFile.get())
            byte[] decryptedBytes = Base64.getDecoder().decode(encryptedPassword.getBytes())
            String decryptedPassword = new String(decryptedBytes)
            return Optional.of(decryptedPassword)
        }
        return Optional.empty()
    }

    static void writePasswordToFile(String clearPassword) {
        byte[] encodedString = Base64.getEncoder().encode(clearPassword.getBytes())
        PropertyService.instance.setProperty(UserProperties.SSH_PASSWORD.key, new String(encodedString))
    }
}

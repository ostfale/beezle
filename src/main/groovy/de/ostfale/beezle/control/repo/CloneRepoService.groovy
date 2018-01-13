package de.ostfale.beezle.control.repo

import com.jcraft.jsch.JSch
import com.jcraft.jsch.JSchException
import com.jcraft.jsch.Session
import de.ostfale.beezle.AppConfig
import groovy.util.logging.Slf4j
import org.eclipse.jgit.api.CloneCommand
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.TransportConfigCallback
import org.eclipse.jgit.lib.TextProgressMonitor
import org.eclipse.jgit.transport.*
import org.eclipse.jgit.util.FS

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Slf4j
class CloneRepoService {

    private static final String REMOTE_URI = "ssh://git@${AppConfig.REPO_SERVER}/"

    void cloneRepository(String localRepoPath, String repoName) {

        SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
            @Override
            protected void configure(OpenSshConfig.Host hc, Session session) {
            }

            @Override
            protected JSch createDefaultJSch(FS fs) throws JSchException {
                if (GitService.getSSHPassword().ifPresent()) {
                    JSch defaultJSch = super.createDefaultJSch(fs)
                    String pkPath = new File("${AppConfig.USER_PROFILE + File.separator}.ssh/id_rsa").getAbsolutePath()
                    defaultJSch.addIdentity(pkPath, GitService.getSSHPassword().get())
                    return defaultJSch
                }
                log.error("Password for SSH key not found!")
                return null;
            }
        }

        Optional<File> repoDirectory = createLocalTargetRepository(localRepoPath, repoName)
        if (!repoDirectory.isPresent()) {
            log.error("Directory for repository could not be created: \n\t repo path: ${localRepoPath} \n\trepo name ${repoName}")
            return
        }

        final String URI = REMOTE_URI + repoName
        final File REPO = repoDirectory.get()
        log.info("Start cloning repository : \n\t URI: ${URI} \n\t Repo:  ${REPO}")
        CloneCommand cloneCommand = Git.cloneRepository()
        cloneCommand.setProgressMonitor(new TextProgressMonitor(new PrintWriter(System.out)))
        cloneCommand.setURI(URI)
        cloneCommand.setDirectory(REPO)
        cloneCommand.setTransportConfigCallback(new TransportConfigCallback() {
            @Override
            void configure(Transport transport) {
                SshTransport sshTransport = (SshTransport) transport;
                sshTransport.setSshSessionFactory(sshSessionFactory);
            }
        })

        Git result = cloneCommand.call()
        result.close()
        log.trace("Finished cloning repository to local drive...")
    }


    static Optional<File> createLocalTargetRepository(String localRepoPath, String repoName) {
        if (Files.exists(Paths.get(localRepoPath))) {
            String targetPathString = localRepoPath + File.separator + repoName
            Path targetPath = Paths.get(targetPathString)
            if (!Files.exists(targetPath)) {
                return Optional.of(Files.createDirectory(targetPath).toFile())
            } else if (targetPath.toFile().listFiles().size() == 0) {
                return Optional.of(targetPath.toFile())
            }
        }
        return Optional.empty()
    }
}

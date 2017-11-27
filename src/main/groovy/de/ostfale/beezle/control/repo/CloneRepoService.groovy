package de.ostfale.beezle.control.repo

import com.jcraft.jsch.JSch
import com.jcraft.jsch.JSchException
import com.jcraft.jsch.Session
import de.ostfale.beezle.AppConfig
import groovy.util.logging.Slf4j
import org.eclipse.jgit.api.CloneCommand
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.TransportConfigCallback
import org.eclipse.jgit.transport.*
import org.eclipse.jgit.util.FS

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
                JSch defaultJSch = super.createDefaultJSch(fs)
                String pkPath = new File("${AppConfig.USER_PROFILE + File.separator}.ssh/id_rsa").getAbsolutePath()
                defaultJSch.addIdentity(pkPath, "mastermind")
                return defaultJSch
            }
        }

        Path repoPath = Paths.get(localRepoPath + File.separator + repoName)
        final String URI = REMOTE_URI + repoName
        final File REPO = repoPath.toFile()
        log.info("Clone repository : \n\t URI: ${URI} \n\t Repo:  ${REPO}")
        CloneCommand cloneCommand = Git.cloneRepository()
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

        /*  Path repoPath = Paths.get(localRepoPath + File.separator + repoName)
          if (Files.exists(repoPath)) {
              final String URI = REMOTE_URL + repoName
              final File REPO = repoPath.toFile()
              Git result = Git.cloneRepository()
                      .setURI(URI)
                      .setDirectory(REPO)
                      .call()
              result.close()
          }*/
        log.error("Local repository (${localRepoPath}) could not be found!")
    }

     File createLocalTargetRepository(String localRepoPath, String repoName) {

    }

}

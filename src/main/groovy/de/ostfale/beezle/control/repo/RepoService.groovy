package de.ostfale.beezle.control.repo

import com.jcabi.ssh.Shell
import com.jcabi.ssh.Ssh
import de.ostfale.beezle.AppConfig
import de.ostfale.beezle.boundary.repo.NodeType
import de.ostfale.beezle.boundary.repo.RepoTreeItem
import de.ostfale.beezle.control.ApplService
import de.ostfale.beezle.control.PropertyService
import de.ostfale.beezle.control.UserProperties
import de.ostfale.beezle.entity.repo.Repo
import de.ostfale.beezle.entity.repo.RepoStatus
import groovy.util.logging.Slf4j
import javafx.scene.control.TreeItem

import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.Path
import java.nio.file.Paths

@Slf4j
class RepoService {

    final static String LOCAL_NODE = 'Local '
    final static String REMOTE_NODE = 'Remote '

    final List<Repo> repoList = new ArrayList<>()

    List<TreeItem<Repo>> getNodeList(String repoPath) {
        List<RepoTreeItem<Repo>> nodeList = new ArrayList<>()
        RepoTreeItem<Repo> localNode = new RepoTreeItem<>(new Repo(LOCAL_NODE, RepoStatus.ROOT), NodeType.NODE)
        RepoTreeItem<Repo> remoteNode = new RepoTreeItem<>(new Repo(REMOTE_NODE, RepoStatus.ROOT), NodeType.NODE)

        repoList.addAll(createRepositoryList())
        repoList.each { Repo repo ->
            RepoTreeItem treeItemWithId = new RepoTreeItem<>(repo, NodeType.LEAF)
            if (repo.repoStatus == RepoStatus.LOCAL) {
                localNode.children.add(treeItemWithId)
            } else {
                remoteNode.children.add(treeItemWithId)
            }
        }
        localNode.getValue().repoName = "$LOCAL_NODE (${localNode.children.size()})"
        remoteNode.getValue().repoName = "$REMOTE_NODE (${remoteNode.children.size()})"
        nodeList.addAll(localNode, remoteNode)
        return nodeList
    }

    private static List<Repo> createRepositoryList() {
        List<Repo> localRepoNames = readLocalRepos()
        List<Repo> allRemoteRepoNames = readRemoteRepos()
        List<Repo> notClonedRepos = allRemoteRepoNames - localRepoNames
        return localRepoNames + notClonedRepos
    }


    private static List<Repo> readLocalRepos(Optional<File> propFile = ApplService.getPropertyFile()) {
        List<Repo> existingRepos = new ArrayList<>()
        if (propFile.isPresent()) {
            String localRepoPath = PropertyService.instance.getProperty(UserProperties.REPO_PATH.key, propFile.get())
            Path filePath = Paths.get(localRepoPath)
            if (Files.exists(filePath, LinkOption.NOFOLLOW_LINKS)) {
                filePath.eachDir { Path foundDir ->
                    Repo repo = new Repo(foundDir.getFileName().toString(), RepoStatus.LOCAL)
                    repo.setRepoPath(foundDir.toFile().getAbsolutePath())
                    existingRepos.add(repo)
                }
            }
        }
        log.trace("Found ${existingRepos.size()} local repositories...")
        return existingRepos
    }

    private static List<Repo> readRemoteRepos(Optional<File> propFile = ApplService.getPropertyFile()) {
        List<Repo> remoteRepos = new ArrayList<>()
        if (propFile.isPresent()) {
            String host = PropertyService.instance.getProperty(UserProperties.BTU_HOST.key, propFile.get())
            String key = new File("${AppConfig.USER_PROFILE + File.separator}.ssh/id_rsa").text
            Shell shell = new Ssh(host, AppConfig.SSH_PORT, AppConfig.SSH_USER, key, "mastermind")
            def result = new Shell.Plain(shell).exec("info")
            result.eachLine { String line ->
                if (line.trim().startsWith('R')) {
                    String trimmedLine = line.trim()
                    String lastEntry = trimmedLine.split('\\s+').last()
                    Repo repo = new Repo(lastEntry, RepoStatus.REMOTE)
                    remoteRepos.add(repo)
                }
            }
        }
        return remoteRepos
    }
}

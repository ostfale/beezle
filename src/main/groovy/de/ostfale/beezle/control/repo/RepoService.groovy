package de.ostfale.beezle.control.repo

import com.jcabi.ssh.Shell
import com.jcabi.ssh.Ssh
import de.ostfale.beezle.AppConfig
import de.ostfale.beezle.control.ApplService
import de.ostfale.beezle.control.PropertyService
import de.ostfale.beezle.control.UserProperties
import de.ostfale.beezle.entity.repo.NodeType
import de.ostfale.beezle.entity.repo.Repo
import de.ostfale.beezle.entity.repo.RepoStatus
import de.ostfale.beezle.entity.repo.TreeItemWithId
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

    List<TreeItem<String>> getNodeList(String repoPath) {
        List<TreeItemWithId<String>> nodeList = new ArrayList<>()
        TreeItemWithId<String> localNode = new TreeItemWithId<>(LOCAL_NODE, NodeType.NODE)
        TreeItemWithId<String> remoteNode = new TreeItemWithId<>(REMOTE_NODE, NodeType.NODE)

        repoList.addAll(createRepositoryList())
        repoList.each { Repo repo ->
            TreeItemWithId treeItemWithId = new TreeItemWithId<>(repo.repoName, NodeType.LEAF, repo.repoName)
            if (repo.repoStatus == RepoStatus.LOCAL) {
                localNode.children.add(treeItemWithId)
            } else {
                remoteNode.children.add(treeItemWithId)
            }
        }
        localNode.setValue("$LOCAL_NODE (${localNode.children.size()})")
        remoteNode.setValue("$REMOTE_NODE (${remoteNode.children.size()})")
        nodeList.addAll(localNode, remoteNode)
        return nodeList
    }

    private static List<Repo> createRepositoryList() {
        List<Repo> allRepoObjects = new ArrayList<>()
        List<String> localRepoNames = readLocalRepos()
        List<String> allRemoteRepoNames = readRemoteRepos()
        List<String> notLoadedRemoteRepos = allRemoteRepoNames - localRepoNames
        localRepoNames.each { String name ->
            allRepoObjects.add(new Repo(name, RepoStatus.LOCAL))
        }
        notLoadedRemoteRepos.each { String name ->
            allRepoObjects.add(new Repo(name, RepoStatus.REMOTE))
        }
        return allRepoObjects
    }


    private static List<String> readLocalRepos(Optional<File> propFile = ApplService.getPropertyFile()) {
        List<String> existingRepos = new ArrayList<>()
        if (propFile.isPresent()) {
            String localRepoPath = PropertyService.instance.getProperty(UserProperties.REPO_PATH.key, propFile.get())
            Path filePath = Paths.get(localRepoPath)
            if (Files.exists(filePath, LinkOption.NOFOLLOW_LINKS)) {
                filePath.eachDir { existingRepos.add(it.getFileName() as String) }
            }
        }
        log.trace("Found ${existingRepos.size()} local repositories...")
        return existingRepos
    }

    private static List<String> readRemoteRepos(Optional<File> propFile = ApplService.getPropertyFile()) {
        List<String> remoteRepos = new ArrayList<>()
        if (propFile.isPresent()) {
            String host = PropertyService.instance.getProperty(UserProperties.BTU_HOST.key, propFile.get())
            String key = new File("${AppConfig.USER_PROFILE + File.separator}.ssh/id_rsa").text
            Shell shell = new Ssh(host, AppConfig.SSH_PORT, AppConfig.SSH_USER, key, "mastermind")
            def result = new Shell.Plain(shell).exec("info")
            result.eachLine { String line ->
                if (line.trim().startsWith('R')) {
                    String trimmedLine = line.trim()
                    String lastEntry = trimmedLine.split('\\s+').last()
                    remoteRepos.add(lastEntry)
                }
            }
        }
        return remoteRepos
    }
}

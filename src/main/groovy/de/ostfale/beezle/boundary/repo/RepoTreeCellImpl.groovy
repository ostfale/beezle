package de.ostfale.beezle.boundary.repo

import de.ostfale.beezle.control.ApplService
import de.ostfale.beezle.control.PropertyService
import de.ostfale.beezle.control.UserProperties
import de.ostfale.beezle.control.repo.CloneTask
import de.ostfale.beezle.entity.repo.Repo
import de.ostfale.beezle.entity.repo.RepoStatus
import groovy.util.logging.Slf4j
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.*

import java.util.concurrent.Future

@Slf4j
class RepoTreeCellImpl extends TreeCell<Repo> {
    @Override
    protected void updateItem(Repo item, boolean empty) {
        super.updateItem(item, empty)

        if (empty) {
            setText(null)
            setGraphic(null)
        } else {
            setText(getString())
            setGraphic(getTreeItem().getGraphic())
            Repo selectedRepoNode = getTreeItem().getValue() as Repo
            if (selectedRepoNode.getRepoStatus() != RepoStatus.ROOT) {
                if (selectedRepoNode.getRepoStatus() != RepoStatus.LOCAL) {
                    ContextMenu ct = new ContextMenu()
                    ct.getItems().addAll(cloneRepository())
                    setContextMenu(ct)
                    // setTooltip(new Tooltip("Clone remote repository into local workspace..."))
                } else if (selectedRepoNode.getRepoStatus() != RepoStatus.REMOTE) {
                    ContextMenu ct = new ContextMenu()
                    ct.getItems().addAll(pullFromRepo(), removeRepository())
                    setContextMenu(ct)
                    //   setTooltip(new Tooltip("Pull changes from repository..."))
                }
            }
        }
    }

    private MenuItem cloneRepository() {
        MenuItem cloneRepo = new MenuItem("Clone repository")
        cloneRepo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            void handle(ActionEvent event) {
                String repoName = getItem().getRepoName()
                Future future = new CloneTask().cloneProjectFromRepository(repoName, PropertyService.instance.getProperty(UserProperties.REPO_PATH.getKey()))
                while (!future.isDone()) {
                    println "Cloning repository ${repoName}"
                    Thread.sleep(300)
                }
                Boolean result = future.get()
                if (result) {
                    RepoTree.refresh()
                    log.info("Cloned repository ${repoName}")
                } else {
                    log.error("Repository ${repoName} could not be cloned!")
                }
            }
        })
        return cloneRepo
    }

    private MenuItem removeRepository() {
        MenuItem menuItem = new MenuItem("Remove Repo")
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            void handle(ActionEvent event) {
                Repo repo = getItem()
                if (!ApplService.deleteDirectory(new File(repo.getRepoPath()))) {
                    log.error("Repository could not be deleted : ${repo.repoPath}")
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Repository ${repo.repoPath} could not be deleted!", ButtonType.OK)
                    alert.setTitle("Error")
                    alert.setHeaderText("Error message")
                    alert.showAndWait()
                }
                RepoTree.refresh()
            }
        })
        return menuItem
    }

    private MenuItem pullFromRepo() {
        MenuItem menuItem = new MenuItem(("Pull from repo"))
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            void handle(ActionEvent event) {
                println "pull from repo ${getItem().getRepoName()}"
            }
        })
        return menuItem
    }

    private String getString() {
        return getItem() == null ? '' : getItem().toString()
    }
}

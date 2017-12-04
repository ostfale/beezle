package de.ostfale.beezle.boundary.repo

import de.ostfale.beezle.CloneTask
import de.ostfale.beezle.control.PropertyService
import de.ostfale.beezle.control.UserProperties
import de.ostfale.beezle.entity.repo.Repo
import de.ostfale.beezle.entity.repo.RepoStatus
import groovy.util.logging.Slf4j
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.control.TreeCell

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
                Future<Boolean> future = new CloneTask().cloneRepository(repoName, PropertyService.instance.getProperty(UserProperties.REPO_PATH.getKey()))
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
                String repoName = getItem().getRepoName()
                log.info("Remove repository ${repoName}")
//                ApplService applService = new ApplService()
//                applService.deleteDirectory()
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
        return getItem() == null ? '' : getItem().toString();
    }
}

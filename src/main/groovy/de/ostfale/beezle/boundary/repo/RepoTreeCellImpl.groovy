package de.ostfale.beezle.boundary.repo

import de.ostfale.beezle.control.PropertyService
import de.ostfale.beezle.control.UserProperties
import de.ostfale.beezle.control.repo.CloneRepoService
import de.ostfale.beezle.entity.repo.Repo
import de.ostfale.beezle.entity.repo.RepoStatus
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.control.Tooltip
import javafx.scene.control.TreeCell

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
                    ct.getItems().add(cloneRepository())
                    setContextMenu(ct)
                    setTooltip(new Tooltip("Clone remote repository into local workspace..."))
                } else if (selectedRepoNode.getRepoStatus() != RepoStatus.REMOTE) {
                    ContextMenu ct = new ContextMenu()
                    ct.getItems().add(pullFromRepo())
                    setContextMenu(ct)
                    setTooltip(new Tooltip("Pull changes from repository..."))
                }
            }
        }
    }

    private MenuItem cloneRepository() {
        MenuItem cloneRepo = new MenuItem("Clone repository")
        cloneRepo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            void handle(ActionEvent event) {
                String localRepoPath = PropertyService.instance.getProperty(UserProperties.REPO_PATH.key)
                CloneRepoService repoService = new CloneRepoService()
                repoService.cloneRepository(localRepoPath, getItem().getRepoName())
            }
        })
        return cloneRepo
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

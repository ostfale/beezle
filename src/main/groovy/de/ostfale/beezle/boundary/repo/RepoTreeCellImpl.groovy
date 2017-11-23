package de.ostfale.beezle.boundary.repo

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
            if (selectedRepoNode.getRepoStatus() != RepoStatus.LOCAL && selectedRepoNode.getRepoStatus() != RepoStatus.ROOT) {
                ContextMenu ct = new ContextMenu()
                ct.getItems().add(cloneRepository())
                setContextMenu(ct)
                setTooltip(new Tooltip("Clone remote repository into local workspace..."))
            }
        }
    }

    private static MenuItem cloneRepository() {
        MenuItem cloneRepo = new MenuItem("Clone repository")
        cloneRepo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            void handle(ActionEvent event) {
                println 'not yet implemented...'
            }
        })
        return cloneRepo
    }

    private String getString() {
        return getItem() == null ? '' : getItem().toString();
    }
}

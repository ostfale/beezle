package de.ostfale.beezle.boundary.repo

import de.ostfale.beezle.control.repo.RepoService
import de.ostfale.beezle.entity.repo.Repo
import de.ostfale.beezle.entity.repo.RepoStatus
import groovy.util.logging.Slf4j
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.control.TreeCell
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.util.Callback

@Slf4j
class RepoTree {

    static TreeView<Repo> treeView
    static RepoPerspective repoPerspective

    static void refresh() {
        treeView.setRoot(createTreeNodes())
    }

    static TreeView createTreeView(RepoPerspective repoPerspective) {
        this.repoPerspective = repoPerspective
        treeView = new TreeView<>(createTreeNodes())
        treeView.styleClass << 'tree'
        treeView.setShowRoot(true)
        treeView.setEditable(false)
        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Repo>>() {
            @Override
            void changed(ObservableValue<? extends TreeItem<Repo>> observable, TreeItem<Repo> oldValue, TreeItem<Repo> newValue) {
                repoPerspective.updateFileInfo(newValue?.getValue())
            }
        })
        setCellFactory(treeView)
        return treeView
    }

    private static RepoTreeItem<Repo> createTreeNodes() {
        def rootIcon = new ImageView(new Image("images/help_16.png"))
        log.trace("Load repo tree configuration...")
        def nodeList = new RepoService().getNodeList("to be fixed...")
        RepoTreeItem<Repo> root = new RepoTreeItem(new Repo('Repositories', RepoStatus.ROOT))
        root.setExpanded(true)
        root.setGraphic(rootIcon)
        root.children.addAll(nodeList)
        return root
    }

    private static setCellFactory(TreeView treeView) {
        treeView.setCellFactory(new Callback<TreeView<Repo>, TreeCell<Repo>>() {
            @Override
            TreeCell call(TreeView<Repo> param) {
                RepoTreeCellImpl repoTreeCell = new RepoTreeCellImpl()
                return repoTreeCell
            }
        })
    }
}

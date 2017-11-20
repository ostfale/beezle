package de.ostfale.beezle.boundary.repo

import de.ostfale.beezle.control.repo.RepoService
import de.ostfale.beezle.entity.repo.TreeItemWithId
import groovy.util.logging.Slf4j
import javafx.scene.control.TreeView
import javafx.scene.image.Image
import javafx.scene.image.ImageView

@Slf4j
class RepoTree {

    static TreeView<String> treeView

    static TreeView createTreeView() {
        treeView = new TreeView<>(createTreeNodes())
        treeView.styleClass << 'tree'
        treeView.setShowRoot(true)
        treeView.setEditable(false)
        return treeView
    }

    private static TreeItemWithId<String> createTreeNodes() {
        def rootIcon = new ImageView(new Image("images/help_16.png"))
        log.trace("Load repo tree configuration...")
        def nodeList = new RepoService().getNodeList("to be fixed...")
        TreeItemWithId<String> root = new TreeItemWithId('Repositories')
        root.setExpanded(true)
        root.setGraphic(rootIcon)
        root.children.addAll(nodeList)
        return root
    }
}

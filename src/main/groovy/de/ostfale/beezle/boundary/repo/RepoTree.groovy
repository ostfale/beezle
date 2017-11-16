package de.ostfale.beezle.boundary.repo

import de.ostfale.beezle.control.repo.RepoTreeService
import de.ostfale.beezle.entity.repo.TreeItemWithId
import groovy.util.logging.Slf4j
import javafx.scene.control.TreeView
import javafx.scene.image.Image
import javafx.scene.image.ImageView

@Slf4j
class RepoTree {

    static TreeView<String> treeView

    private static TreeItemWithId<String> createTreeNodes() {
        def rootIcon = new ImageView(new Image("help_16.png"))
        //      File treeCfg = new AppConfig().getDatabaseTreeFile()
        log.trace("Load tree configuration file from ${treeCfg.getAbsolutePath()}")
        def nodeList = new RepoTreeService().getNodeList("to be fixed...")
        TreeItemWithId<String> root = new TreeItemWithId('GlobeData')
        root.setExpanded(true)
        root.setGraphic(rootIcon)
        root.children.addAll(nodeList)
        return root
    }
}

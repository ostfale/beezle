package de.ostfale.beezle.entity.repo

import groovy.util.logging.Slf4j
import javafx.scene.control.TreeItem
import javafx.scene.image.Image
import javafx.scene.image.ImageView

@Slf4j
class TreeItemWithId<T> extends TreeItem<T> {

    final String nodeId
    NodeType nodeType

    TreeItemWithId(T var1) {
        super(var1)
        this.nodeType = NodeType.NODE
        nodeId = ''
    }

    TreeItemWithId(T var1, NodeType aNodeType) {
        super(var1)
        this.nodeType = aNodeType
        setAppropriateIcon(aNodeType)
        nodeId = ''
    }

    TreeItemWithId(T var1, final NodeType aNodeType, String nodeId) {
        super(var1)
        this.nodeId = nodeId
        this.nodeType = aNodeType
        setAppropriateIcon(aNodeType)
    }

    private void setAppropriateIcon(NodeType aNodeType) {
        switch (aNodeType) {
            case NodeType.NODE:
            case NodeType.SUBNODE:
                super.setGraphic(new ImageView(new Image("images/folder_16.png")))
                break
            case NodeType.LEAF:
                super.setGraphic(new ImageView(new Image("images/key_16.png")))
                break
            default:
                break
        }
    }
}

enum NodeType {
    NODE, SUBNODE, LEAF
}
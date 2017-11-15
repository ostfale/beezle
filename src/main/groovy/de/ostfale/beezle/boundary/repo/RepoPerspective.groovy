package de.ostfale.beezle.boundary.repo

import de.ostfale.beezle.boundary.IPerspective
import groovy.util.logging.Slf4j
import groovyx.javafx.SceneGraphBuilder
import javafx.collections.FXCollections
import javafx.scene.Node
import javafx.scene.layout.Pane

@Slf4j
class RepoPerspective implements IPerspective {

    final SceneGraphBuilder sgb

    RepoPerspective(SceneGraphBuilder sgb) {
        this.sgb = sgb
    }

    @Override
    void updatePerspective() {

    }

    @Override
    Pane getLeftSideView() {
        return null
    }

    @Override
    Pane getCenterView() {
        return null
    }

    @Override
    Collection<Node> getStatusBarElements() {
        return FXCollections.emptyObservableList()
    }
}

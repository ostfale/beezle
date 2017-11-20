package de.ostfale.beezle.boundary.repo

import de.ostfale.beezle.boundary.IPerspective
import groovy.util.logging.Slf4j
import groovyx.javafx.SceneGraphBuilder
import javafx.collections.FXCollections
import javafx.scene.Node
import javafx.scene.layout.GridPane
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
        GridPane pane = sgb.gridPane(padding: 1) {
            rowConstraints(vgrow: 'always')
            node(RepoTree.createTreeView())
        }
        return pane
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

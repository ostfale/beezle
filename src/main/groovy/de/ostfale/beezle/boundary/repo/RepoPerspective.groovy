package de.ostfale.beezle.boundary.repo

import de.ostfale.beezle.boundary.IPerspective
import groovy.util.logging.Slf4j
import groovyx.javafx.SceneGraphBuilder
import javafx.collections.FXCollections
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane

@Slf4j
class RepoPerspective implements IPerspective {

    final static String NAME = "Repository Perspective"
    final SceneGraphBuilder sgb

    RepoPerspective(SceneGraphBuilder sgb) {
        this.sgb = sgb
    }

    @Override
    String getName() {
        return NAME
    }

    @Override
    void updatePerspective() {
        RepoTree.refresh()
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
        Label name = new Label(NAME)
        return FXCollections.observableArrayList(name)
    }
}

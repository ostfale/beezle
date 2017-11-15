package de.ostfale.beezle.boundary

import javafx.scene.Node
import javafx.scene.layout.Pane

interface IPerspective {

    void updatePerspective()

    Pane getLeftSideView()

    Pane getCenterView()

    Collection<Node> getStatusBarElements()
}
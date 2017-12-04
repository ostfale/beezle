package de.ostfale.beezle.boundary

import javafx.scene.Node
import javafx.scene.layout.Pane

interface IPerspective {

    String getName()

    void updatePerspective()

    Pane getLeftSideView()

    Pane getCenterView()

    Collection<Node> getStatusBarElements()
}
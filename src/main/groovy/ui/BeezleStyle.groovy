package ui

import groovyx.javafx.SceneGraphBuilder
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import javafx.stage.StageStyle

class BeezleStyle {

    static SceneGraphBuilder sgb

    static style(SceneGraphBuilder sceneGraphBuilder) {
        sgb = sceneGraphBuilder
        Stage frame = sgb.primaryStage
        frame.title = "Beezle"
        frame.setOpacity(0.95)
        frame.initStyle(StageStyle.DECORATED)
        Scene scene = frame.scene
        scene.stylesheets << 'style.css'

        BorderPane borderPane = scene.root as BorderPane
        borderPane.styleClass << 'form'
    }
}

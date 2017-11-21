package de.ostfale.beezle.boundary

import de.ostfale.beezle.AppConfig
import groovyx.javafx.SceneGraphBuilder
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import javafx.stage.StageStyle

class BeezleStyle {

    static SceneGraphBuilder sgb

    static style(SceneGraphBuilder sceneGraphBuilder) {
        sgb = sceneGraphBuilder
        Stage frame = sgb.primaryStage
        frame.getIcons().addAll(new Image('images/help_16.png'), new Image('images/help_32.png'))
        frame.title = "${AppConfig.APP_NAME} - ${AppConfig.APP_VERSION}"
        frame.setOpacity(0.95)
        frame.initStyle(StageStyle.DECORATED)
        Scene scene = frame.scene
        scene.stylesheets << 'style.css'

        BorderPane borderPane = scene.root as BorderPane
        borderPane.styleClass << 'form'
    }
}

package ui

import control.ResourceService
import groovy.util.logging.Slf4j
import groovyx.javafx.GroovyFX
import groovyx.javafx.SceneGraphBuilder
import javafx.scene.control.Button
import javafx.scene.control.Tooltip
import javafx.scene.layout.BorderPane
import javafx.stage.Stage

@Slf4j
class BeezleUI {

    SceneGraphBuilder sceneGraphBuilder
    Stage stage
    BorderPane bp

    void startUI() {
        GroovyFX.start { app ->
            SceneGraphBuilder builder = delegate as SceneGraphBuilder
            layoutFrame(builder)
            BeezleStyle.style(builder)
            builder.primaryStage.show()
        }
    }

    def layoutFrame(SceneGraphBuilder sgb) {
        sceneGraphBuilder = sgb
        stage = sgb.stage {
            scene {
                bp = borderPane {
                    top(margin: [2, 0, 1, 0]) {
                        vbox() {
                            node(createToolBar())
                        }
                    }
                }
            }
        }
    }

    def createToolBar() {
        def exitButton = new Button(font: ResourceService.FA14, text: ResourceService.ICON_EXIT, onAction: exitAction, id: 'exitBtn', tooltip: new Tooltip('Exit application'))
    }

    def exitAction = { sceneGraphBuilder.primaryStage.close() }
}

package de.ostfale.beezle.boundary

import de.ostfale.beezle.AppConfig
import de.ostfale.beezle.boundary.repo.RepoPerspective
import de.ostfale.beezle.control.ResourceService
import groovy.util.logging.Slf4j
import groovyx.javafx.GroovyFX
import groovyx.javafx.SceneGraphBuilder
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.stage.Stage

@Slf4j
class BeezleUI {

    SceneGraphBuilder sceneGraphBuilder
    Stage stage
    BorderPane bp

    private DefaultStatusBar statusBar

    // perspectives
    RepoPerspective repoPerspective

    void startUI() {
        GroovyFX.start { app ->
            SceneGraphBuilder builder = delegate as SceneGraphBuilder
            initPerspective(builder)
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
                            node(createMenuBar())
                            node(createToolBar())
                        }
                    }
                    left() {
                        repoPerspective.getLeftSideView()
                    }

                    right(margin: 1) {
                        toolBar(orientation: VERTICAL) {
                            new ResourceService().with {
                                button(font: FA16, text: ICON_GITLAB, tooltip: new Tooltip("Repository Perspective"), onAction: activateRepoPerspective)
                            }
                        }
                    }
                    bottom(margin: 1) {
                        node(statusBar.getDefaultStatusBar())
                    }
                }
            }
        }
    }

    def createToolBar() {
        def exitButton = new Button(font: ResourceService.FA14, text: ResourceService.ICON_EXIT, onAction: exitAction, id: 'exitBtn', tooltip: new Tooltip('Exit application'))
    }

    def createMenuBar() {
        def mb = new MenuBar()
        mb.menus.add(createFileMenu())
        mb.menus.add(createHelpMenu())
        return mb
    }

    def createFileMenu() {
        def fm = new Menu(text: 'File')
        fm.items.add(new MenuItem(text: 'Exit', onAction: exitAction))
        return fm
    }

    def createHelpMenu() {
        def hm = new Menu(text: 'Help')
        hm.items.add(new MenuItem(text: 'About', onAction: aboutAction))
        return hm
    }

    def aboutAction = {
        Alert alert = new Alert(Alert.AlertType.INFORMATION)
        alert.setTitle("About ${AppConfig.APP_NAME}")
        alert.setHeaderText("${AppConfig.APP_NAME} v${AppConfig.APP_VERSION}");
        alert.setContentText("Contact: info@uwe-sauerbrei.de");
        alert.showAndWait();
    }

    def exitAction = { sceneGraphBuilder.primaryStage.close() }

    def activateRepoPerspective = {
        println "show repo perspective..."
    }

    private initPerspective(SceneGraphBuilder sceneGraphBuilder1) {
        this.statusBar = new DefaultStatusBar()
        this.repoPerspective = new RepoPerspective(sceneGraphBuilder1)
    }
}

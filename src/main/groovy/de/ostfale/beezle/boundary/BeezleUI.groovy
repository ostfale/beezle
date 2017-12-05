package de.ostfale.beezle.boundary

import de.ostfale.beezle.AppConfig
import de.ostfale.beezle.boundary.repo.RepoPerspective
import de.ostfale.beezle.boundary.soap.SoapPerspective
import de.ostfale.beezle.control.ApplService
import de.ostfale.beezle.control.ResourceService
import de.ostfale.beezle.control.SetupDialog
import groovy.util.logging.Slf4j
import groovyx.javafx.GroovyFX
import groovyx.javafx.SceneGraphBuilder
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.stage.Stage

@Slf4j
class BeezleUI {

    SceneGraphBuilder sceneGraphBuilder
    Stage stage
    BorderPane bp

    private DefaultStatusBar statusBar

    // dialogs
    SetupDialog setupDialog

    // perspectives
    RepoPerspective repoPerspective
    SoapPerspective soapPerspective

    void startUI() {
        GroovyFX.start { app ->
            SceneGraphBuilder builder = delegate as SceneGraphBuilder
            if (!ApplService.foundApplicationDirectory()) {
                setupDialog = new SetupDialog(builder)
                setupDialog.dlg.show()
            } else {
                initPerspective(builder)
                layoutFrame(builder)
                BeezleStyle.style(builder)
                builder.primaryStage.show()
            }
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
                                button(font: FA16, text: ICON_GITLAB, tooltip: new Tooltip("Repository Perspective"), onAction: {
                                    setPerspective(repoPerspective)
                                })
                                button(font: FA16, text: ICON_PENCIL, tooltip: new Tooltip("SOAP Perspective"), onAction: {
                                    setPerspective(soapPerspective)
                                })
                            }
                        }
                    }
                    bottom(margin: 1) {
                        node(statusBar.getDefaultStatusBar())
                    }
                    center(margin: [1, 0, 5, 1]) {
                        repoPerspective.getCenterView()
                    }
                }
            }
        }
    }

    def createToolBar() {
        final int spacing = 5
        def exitButton = new Button(font: ResourceService.FA14, text: ResourceService.ICON_EXIT, onAction: exitAction, id: 'exitBtn', tooltip: new Tooltip('Exit application'))
        def refreshButton = new Button(font: ResourceService.FA14, text: ResourceService.ICON_REFRESH, onAction: refreshAction, id: 'refreshBtn', tooltip: new Tooltip('Refresh views'))
        def aboutButton = new Button(font: ResourceService.FA14, text: ResourceService.ICON_INFO, onAction: aboutAction, id: 'aboutBtn', tooltip: new Tooltip('Show Info'))

        final HBox leftSection = new HBox(exitButton, new Separator(orientation: Orientation.VERTICAL), refreshButton)
        final HBox rightSection = new HBox(aboutButton, new Separator(orientation: Orientation.VERTICAL))
        leftSection.setAlignment(Pos.CENTER_LEFT)
        leftSection.setSpacing(spacing)
        rightSection.setAlignment(Pos.CENTER_RIGHT)
        rightSection.setSpacing(spacing)
        HBox.setHgrow(leftSection, Priority.ALWAYS)
        HBox.setHgrow(rightSection, Priority.ALWAYS)
        def tb = new ToolBar(orientation: Orientation.HORIZONTAL)
        tb.setPadding(new Insets(spacing))
        tb.getItems().addAll(leftSection, rightSection)
        return tb
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
        alert.setHeaderText("${AppConfig.APP_NAME} v${AppConfig.APP_VERSION}")
        alert.setContentText("Contact: info@uwe-sauerbrei.de")
        alert.showAndWait()
    }

    def exitAction = { sceneGraphBuilder.primaryStage.close() }
    def refreshAction = { repoPerspective.updatePerspective() }

    def setPerspective = { IPerspective perspective ->
        if (bp) {
            log.trace("Switch to ${perspective.getName()}")
            bp.setLeft(perspective.getLeftSideView())
            bp.setCenter(perspective.getCenterView())
            statusBar.updateCustomLabels(perspective.getStatusBarElements())
        }
    }

    private initPerspective(SceneGraphBuilder sceneGraphBuilder1) {
        this.statusBar = new DefaultStatusBar()
        this.repoPerspective = new RepoPerspective(sceneGraphBuilder1)
        this.soapPerspective = new SoapPerspective(sceneGraphBuilder1)
    }
}

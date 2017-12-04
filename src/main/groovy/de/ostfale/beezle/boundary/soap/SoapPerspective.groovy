package de.ostfale.beezle.boundary.soap

import de.ostfale.beezle.boundary.IPerspective
import groovy.util.logging.Slf4j
import groovyx.javafx.SceneGraphBuilder
import javafx.collections.FXCollections
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory

/**
 * Analyse and check SOAP files
 * Created by usauerbrei on 01.12.2017
 */
@Slf4j
class SoapPerspective implements IPerspective {

    final static String NAME = 'SOAP Message Perspective'

    final SceneGraphBuilder sgb
    CodeArea area

    SoapPerspective(SceneGraphBuilder sgb) {
        this.sgb = sgb
    }

    @Override
    String getName() {
        return NAME
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
        GridPane gridPane = sgb.gridPane(hgap: 50, vgap: 5, padding: 1, alignment: "top_center") {
            rowConstraints()
            rowConstraints(vgrow: 'always')
            node(createEditorPane(), row: 1, column: 0, hgrow: 'always')
        }
        return gridPane
    }

    @Override
    Collection<Node> getStatusBarElements() {
        Label name = new Label(NAME)
        return FXCollections.observableArrayList(name)
    }

    def createEditorPane() {
        area = new CodeArea()
        area.setEditable(true)
        area.setParagraphGraphicFactory(LineNumberFactory.get(area))
        return area
    }
}

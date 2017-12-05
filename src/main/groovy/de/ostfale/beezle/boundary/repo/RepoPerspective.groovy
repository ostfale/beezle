package de.ostfale.beezle.boundary.repo

import de.ostfale.beezle.boundary.IPerspective
import de.ostfale.beezle.entity.repo.Repo
import groovy.util.logging.Slf4j
import groovyx.javafx.SceneGraphBuilder
import javafx.collections.FXCollections
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane

@Slf4j
class RepoPerspective implements IPerspective {

    final static String NAME = "Repository Perspective"
    final SceneGraphBuilder sgb
    Label lblRepoPath, lblRepoSize
    TextArea taReadme

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
            node(RepoTree.createTreeView(this))
        }
        return pane
    }

    @Override
    Pane getCenterView() {
        GridPane gridPane = sgb.gridPane(hgap: 50, vgap: 5, padding: 1, alignment: "top_center") {
            columnConstraints(minWidth: 100, halignment: 'left')
            columnConstraints(prefWidth: 250, hgrow: 'always')
            rowConstraints()
            rowConstraints()
            rowConstraints(vgrow: 'always')
            effect innerShadow()
            label(id: 'fLabel', text: "Repository Path", row: 0, column: 0)
            lblRepoPath = label(id: 'fContent', text: "", row: 0, column: 1)
            label(id: 'fLabel', text: "Repository Size", row: 1, column: 0)
            lblRepoSize = label(id: 'fContent', text: "", row: 1, column: 1)
            node(taReadme = createReadmeDisplay(), row: 2, column: 0, columnSpan: 2)
        }
        return gridPane
    }

    @Override
    Collection<Node> getStatusBarElements() {
        Label name = new Label(NAME)
        return FXCollections.observableArrayList(name)
    }

    void updateFileInfo(Repo selectedRepo) {
        lblRepoPath.setText(selectedRepo?.getRepoPath())
        taReadme.setText(selectedRepo?.getReadme())
    }

    def createReadmeDisplay() {
        TextArea area = new TextArea()
        area.setEditable(false)
        return area
    }
}

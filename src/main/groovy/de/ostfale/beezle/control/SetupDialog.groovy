package de.ostfale.beezle.control

import groovyx.javafx.SceneGraphBuilder
import javafx.beans.value.ChangeListener
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.stage.Modality
import javafx.stage.Stage

/**
 * Ask for initial setup parameter:
 * <ul>
 *     <li> repository location
 *     <li> key file location
 *     <li> key file password
 * </ul>
 * Created by usauerbrei on 30.11.2017
 */
class SetupDialog implements MessageDialog {

    final Stage dlg
    final List<TextField> txtFieldList = new ArrayList<>()
    TextField tfRepoDir, tfKeyFileDir, tfPWUnmasked
    PasswordField tfPWMasked
    Button btnSave

    SetupDialog(SceneGraphBuilder sgb) {
        dlg = new Stage()
        dlg.initOwner(sgb.primaryStage)
        dlg.initModality(Modality.APPLICATION_MODAL)
        dlg.setScene(initDialog())
        dlg.setTitle("Setup Repository parameter")
    }

    private Scene initDialog() {
        def grid = createGridPane()
        def btnBar = createButtonBar()
        BorderPane borderPane = new BorderPane()
        borderPane.setPrefSize(600, 200)
        borderPane.setMargin(grid, new Insets(10))
        borderPane.setMargin(btnBar, new Insets(5, 5, 5, 5))
        borderPane.setCenter(grid)
        borderPane.setBottom(createButtonBar())
        return new Scene(borderPane)
    }

    private GridPane createGridPane() {
        def lblRepo = new Label("Repository Location")
        def lblKeyFile = new Label("SSH Key File Location")
        GridPane gpane = new GridPane()
        gpane.setHgap(20)
        gpane.setVgap(10)
        gpane.add(lblRepo, 0, 0)
        gpane.add(tfRepoDir = createTextField(), 1, 0)
        GridPane.setHgrow(tfRepoDir, Priority.ALWAYS)
        gpane.add(lblKeyFile, 0, 1)
        gpane.add(tfKeyFileDir = createTextField(), 1, 1)
        gpane.setGridLinesVisible(false)
        return gpane
    }

    private def createButtonBar() {
        HBox hBox = new HBox(10, btnSave = createSaveButton(), createCancelButton())
        hBox.setAlignment(Pos.CENTER_RIGHT)
        return hBox
    }

    private static def createSaveButton() {
        Button btn = new Button("Save")
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            void handle(ActionEvent event) {
                println "save setup parameter..."
            }
        })
        return btn
    }

    private static def createCancelButton() {
        Button btn = new Button("Close")
        btn.setCancelButton(true)
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            void handle(ActionEvent event) {
                System.exit(0)
            }
        })
        return btn
    }

    private TextField createTextField(String txt = "") {
        TextField textField = new TextField()
        textField.textProperty().addListener({ observableValue, oldValue, newValue -> checkSaveButtonStatus() } as ChangeListener)
        textField.setPromptText(txt)
        return textField
    }

    void checkSaveButtonStatus() {
        boolean disable = false
        txtFieldList.collect {
            if (!it || !it.getText() || !it.getText().trim().isEmpty()) {
                disable = true
                return
            }
        }
        btnSave.setDisable(disable)
    }
}

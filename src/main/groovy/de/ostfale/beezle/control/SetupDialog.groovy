package de.ostfale.beezle.control

import de.ostfale.beezle.AppConfig
import groovy.util.logging.Slf4j
import groovyx.javafx.SceneGraphBuilder
import javafx.beans.value.ChangeListener
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import javafx.stage.Modality
import javafx.stage.Stage

import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Ask for initial setup parameter:
 * <ul>
 *     <li> repository location
 *     <li> key file location
 *     <li> key file password
 * </ul>
 * Created by usauerbrei on 30.11.2017
 */
@Slf4j
class SetupDialog implements MessageDialog {

    final Stage dlg
    final List<TextField> txtFieldList = new ArrayList<>()
    TextField tfRepoDir, tfKeyFileDir, tfPWUnmasked
    PasswordField tfPWMasked
    Button btnSave, btnPWUnmask
    boolean unmask = false

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
        borderPane.setMargin(btnBar, new Insets(5, 15, 5, 5))
        borderPane.setCenter(grid)
        borderPane.setBottom(createButtonBar())
        return new Scene(borderPane)
    }

    private GridPane createGridPane() {
        initUnmaskablePasswordField()
        def lblRepo = new Label("Repository Location")
        def lblKeyFile = new Label("SSH Key File Location")
        GridPane gpane = new GridPane()
        gpane.setHgap(20)
        gpane.setVgap(10)
        gpane.add(lblRepo, 0, 0)
        gpane.add(tfRepoDir = createTextField(), 1, 0)
        GridPane.setHgrow(tfRepoDir, Priority.ALWAYS)
        gpane.add(createRepoButton(), 2, 0)
        gpane.add(lblKeyFile, 0, 1)
        gpane.add(tfKeyFileDir = createKeyFileTextField(), 1, 1)
        gpane.add(createKeyFileButton(), 2, 1)
        gpane.add(new Label("Password"), 0, 2)
        gpane.add(tfPWMasked, 1, 2)
        gpane.add(tfPWUnmasked, 1, 2)
        gpane.add(createPasswordButton(), 2, 2)
        gpane.setGridLinesVisible(false)
        txtFieldList.addAll(tfKeyFileDir, tfRepoDir, tfPWMasked, tfPWUnmasked)
        return gpane
    }

    private TextField createKeyFileTextField() {
        def tf = createTextField()
        String result = PropertyService.instance.getProperty(UserProperties.SSH_KEY_LOCATION.key)
        if (result) {
            tf.setText(result)
        } else {
            Path defaultSSHPath = Paths.get(AppConfig.SSH_DEFAULT)
            if (Files.exists(defaultSSHPath, LinkOption.NOFOLLOW_LINKS)) {
                tf.setText(AppConfig.SSH_DEFAULT)
            }
        }
        return tf
    }

    private void initUnmaskablePasswordField() {
        // show password unmasked
        tfPWUnmasked = new TextField()
        tfPWUnmasked.setManaged(false)
        tfPWUnmasked.setVisible(false)
        // actual password file
        tfPWMasked = new PasswordField()
        // Bind the textField and passwordField text values bidirectionally.
        tfPWUnmasked.textProperty().bindBidirectional(tfPWMasked.textProperty())
        btnPWUnmask = new Button(font: ResourceService.FA14, text: ResourceService.ICON_EYE)
    }

    private Button createPasswordButton() {
        Button btn = new Button(font: ResourceService.FA14, text: ResourceService.ICON_EYE, disable: false)
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            void handle(ActionEvent event) {
                if (unmask) {
                    tfPWMasked.setVisible(true)
                    tfPWMasked.setManaged(true)
                    tfPWUnmasked.setVisible(false)
                    tfPWUnmasked.setManaged(false)
                    unmask = false
                } else {
                    tfPWUnmasked.setVisible(true)
                    tfPWUnmasked.setManaged(true)
                    tfPWMasked.setVisible(false)
                    tfPWMasked.setManaged(false)
                    unmask = true
                }
            }
        })
        return btn
    }

    private def createButtonBar() {
        HBox hBox = new HBox(15, btnSave = createSaveButton(), createCancelButton())
        hBox.setAlignment(Pos.CENTER_RIGHT)
        return hBox
    }

    private def createRepoButton() {
        return new Button(font: ResourceService.FA14, text: ResourceService.ICON_FOLDER, tooltip: new Tooltip('Select local git repo'), onAction: showRepoFC)
    }

    private def createKeyFileButton() {
        return new Button(font: ResourceService.FA14, text: ResourceService.ICON_FILE, tooltip: new Tooltip('Select SSH key file location'), onAction: showKeyFileFC)
    }

    private def createSaveButton() {
        Button btn = new Button("Save")
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            void handle(ActionEvent event) {
                saveProperties()
            }
        })
        btn.setDisable(false)
        return btn
    }

    private saveProperties() {
        String repoDir = tfRepoDir.getText().trim()
        String keyFileDir = tfKeyFileDir.getText().trim()
        PropertyService.instance.setProperty(UserProperties.REPO_PATH.key, repoDir)
        PropertyService.instance.setProperty(UserProperties.SSH_KEY_LOCATION.key, keyFileDir)
        log.info("Saved intial parameter: \n\t Repo-dir: $repoDir \n\tSSH key file: $keyFileDir")
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

    private TextField createTextField(String aText = "") {
        TextField textField = new TextField()
        textField.textProperty().addListener({ observableValue, oldValue, newValue ->
            println "do check for old: $oldValue and new $newValue and observ $observableValue"
            checkSaveButtonStatus()
        } as ChangeListener)
        textField.setPromptText(aText)
        return textField
    }

    // TODO check save button status
    void checkSaveButtonStatus() {
        boolean disable = false
        /*   txtFieldList.collect {
               if (!it || !it.getText() || it.getText().trim().isEmpty()) {
                   disable = true
                   return
               }
           }*/
//        btnSave.setDisable(disable)
    }

    def showRepoFC = {
        DirectoryChooser dirDialog = new DirectoryChooser();
        dirDialog.setTitle("Selecte local repository folder")
        dirDialog.setInitialDirectory(new File("c:\\"))
        File result = dirDialog.showDialog(dlg)
        if (result) {
            log.trace("Setup: Selected repository directory : ${result.getAbsolutePath()}")
            tfRepoDir.setText(result.getAbsolutePath())
        }
    }

    def showKeyFileFC = {
        FileChooser fileChooser = new FileChooser()
        fileChooser.setTitle("Select SSH key file")
        fileChooser.setInitialDirectory(new File(AppConfig.USER_PROFILE))
        File result = fileChooser.showOpenDialog(dlg)
        if (result) {
            log.trace("Setup: Selected SSH key file : ${result.getAbsolutePath()}")
            tfKeyFileDir.setText(result.getAbsolutePath())
        }
    }
}

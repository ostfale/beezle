package de.ostfale.beezle.control

import javafx.scene.control.Alert

trait MessageDialog {

    void createErrorDialog(String headerText, String errorText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle('Application Error')
        alert.setHeaderText(headerText)
        alert.setContentText(errorText)
        alert.showAndWait()
    }

    void createInfoDialog(String headerText, String errorText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle('Application Information')
        alert.setHeaderText(headerText)
        alert.setContentText(errorText)
        alert.showAndWait()
    }
}
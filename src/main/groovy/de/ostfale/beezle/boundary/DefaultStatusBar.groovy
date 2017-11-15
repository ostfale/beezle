package de.ostfale.beezle.boundary

import groovy.transform.Canonical
import javafx.beans.property.*
import javafx.scene.control.Label
import javafx.scene.control.Separator
import org.codehaus.groovy.runtime.DateGroovyMethods
import org.controlsfx.control.StatusBar

class DefaultStatusBar {

    final StatusBar

    DefaultStatusBar() {
        this.StatusBar = new StatusBar()
    }

    StatusBar getDefaultStatusBar() {
        InfoDisplay infoDisplay = new InfoDisplay()
        Label currentUser = new Label()
        Label currentDate = new Label()
        currentUser.textProperty().bind(infoDisplay.userNameProperty())
        currentDate.textProperty().bind(infoDisplay.userDate)
        statusBar.getRightItems().addAll(currentUser, new Separator(), currentDate, new Separator())
        statusBar.getLeftItems().clear()
        statusBar.setText('')
        return statusBar
    }

    void updateCustomLabels(Collection<Node> elements) {
        statusBar.getLeftItems().clear()
        statusBar.getLeftItems().addAll(elements)
    }

}

@Canonical
class InfoDisplay {

    public static final String USERNAME = 'USERNAME'

    private final ReadOnlyStringWrapper userName
    final StringProperty userDate
    final IntegerProperty hours
    final IntegerProperty minutes
    final IntegerProperty seconds

    InfoDisplay() {
        userName = new ReadOnlyStringWrapper(this, "user", System.getenv().get(USERNAME))
        userDate = new SimpleStringProperty(this, "date", DateGroovyMethods.format(new Date(), 'dd-MM-yyyy'))
        hours = new SimpleIntegerProperty(this, "hours", 0)
        minutes = new SimpleIntegerProperty(this, "minutes", 0)
        seconds = new SimpleIntegerProperty(this, "seconds", 0)
    }

    final ReadOnlyStringProperty userNameProperty() {
        return userName.getReadOnlyProperty();
    }
}
package de.ostfale.beezle.control

import de.ostfale.beezle.AppConfig
import groovy.util.logging.Slf4j
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Slf4j
class ApplService {

    static Optional<File> getPropertyFile(String baseDirPath = AppConfig.USER_PROFILE) {
        log.trace("Search for application property file...")

        if (baseDirPath) {
            String propFilePath = baseDirPath + File.separator + AppConfig.APP_NAME + File.separator + AppConfig.PROPERTY_FILE_NAME
            Path propFile = Paths.get(propFilePath)
            if (Files.exists(propFile)) {
                log.trace("User property file found : ${propFilePath.toString()}")
                return Optional.of(propFile.toFile())
            }
            File newPropFile = createPropertyFile(propFilePath)
            return Optional.of(newPropFile)
        }

        log.error("User directory could not be found...")
        return Optional.empty()
    }

    static boolean foundApplicationDirectory() {
        return Files.exists(Paths.get(AppConfig.PROPERTY_DEFAULT))
    }

    static boolean deleteDirectory(File directoryToBeDeleted) {
        if (directoryToBeDeleted.exists()) {
            log.trace("Delete directory : ${directoryToBeDeleted.getAbsolutePath()}")
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want delete local repository:  ${directoryToBeDeleted.getAbsolutePath()}?")
            alert.setTitle("Confirmation")
            alert.setHeaderText("Repo deletion confirmation")
            Optional<ButtonType> result = alert.showAndWait()
            if (result.isPresent() && result.get() == ButtonType.OK) {
                return directoryToBeDeleted.deleteDir()
            }
        }
        return false
    }

    private static File createPropertyFile(String targetPath) {
        log.trace("Create new property file: $targetPath")
        File propertyFile = new File(targetPath)
        propertyFile.getParentFile().mkdirs()
        propertyFile << '# user defined properties\n'
        return propertyFile
    }
}

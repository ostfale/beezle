package de.ostfale.beezle.control

import de.ostfale.beezle.AppConfig
import groovy.util.logging.Slf4j

import java.nio.file.Files
import java.nio.file.LinkOption
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

    private static File createPropertyFile(String targetPath) {
        log.trace("Create new property file: $targetPath")
        File propertyFile = new File(targetPath)
        propertyFile.getParentFile().mkdirs()
        propertyFile << '# user defined properties\n'
        return propertyFile
    }

    static Optional<File> createDirectory(String dirPath, String dirName) {
        log.trace("Create new directy ${dirName} for the given path ${dirPath}")
        Path rootPath = Paths.get(dirPath)
        if (Files.exists(rootPath, LinkOption.NOFOLLOW_LINKS)) {
            File newDirecty = new File(dirPath + File.separator + dirName)
            if (newDirecty.getParentFile().mkdirs()) {
                return Optional.of(newDirecty)
            }
            log.error("Directory ${newDirecty.getAbsolutePath()} could not be created or does already exist...")
        }
        log.error("Root directory ${dirPath} does not exist...")
        return Optional.empty()
    }
}

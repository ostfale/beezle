package de.ostfale.beezle.control

import de.ostfale.beezle.AppConfig
import groovy.util.logging.Slf4j

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Slf4j
class ApplService {

    static final String ENV_USER_PROFILE = "USERPROFILE"

    Optional<File> getProperytFile() {
        log.trace("Search for application property file...")
        Optional<String> baseDirPath = getUserDirPath()
        if (baseDirPath.isPresent()) {
            final String propFileString = baseDirPath.get() + AppConfig.APP_NAME + File.separator + AppConfig.PROPERTY_FILE_NAME
            Path propFilePath = Paths.get(propFileString)
            if (Files.exists(propFilePath)) {
                log.trace("User property file found : ${propFilePath.toString()}")
                return Optional.of(propFilePath.toFile())
            }

        }
        log.error("User directory could not be found...")
        return Optional.empty()
    }

    private File createPropertyFile(String path = AppConfig.APP_NAME + File.separator + AppConfig.PROPERTY_FILE_NAME) {

        String targetPath = System.getenv(ENV_USER_PROFILE) + File.separator + path
        File propertyFile = new File(targetPath)
        propertyFile << 'User defined properties\n'
        return propertyFile
    }

    private Optional<String> getUserDirPath() {
        def result = System.getenv(ENV_USER_PROFILE)
        log.trace("User profile  directory found: ${result}")
        return Optional.ofNullable(result)
    }
}

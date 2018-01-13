package de.ostfale.beezle.control

import groovy.util.logging.Slf4j

@Slf4j
@Singleton
class PropertyService {

    Properties properties = new Properties()
    static final File DEFAULT_PROP_FILE = ApplService.getPropertyFile().get()

    String getProperty(final String aKey, File propertyFile = DEFAULT_PROP_FILE) {
        properties = !properties.isEmpty() ? properties : loadProperties(propertyFile)
        String value = properties.getProperty(aKey)
        log.trace("Read property ${aKey} => found: ${value}")
        return value
    }

    void setProperty(String key, String aValue, File propertyFile = DEFAULT_PROP_FILE) {
        properties = !properties.isEmpty() ? properties : loadProperties(propertyFile)
        log.trace("Write property ${key} with value ${aValue}")
        properties.setProperty(key, aValue)
        storeProperties(ApplService.getPropertyFile().get().getAbsolutePath())
    }

    /**
     * Load property file from a given directory. If the file does not exist an empty property file is created to be used to store
     * user and application data.
     * @param propertyDir
     * @return
     */
    private static Properties loadProperties(File userPropFile) {
        log.debug("Read user properties from file: ${userPropFile.getCanonicalPath()}")
        Properties props = new Properties()
        InputStream iStream = userPropFile.newDataInputStream()
        props.load(iStream)
        iStream.close()
        return props
    }

    private void storeProperties(String propPath) {
        log.debug('store properties...')
        properties.store(new File(propPath).newWriter(), '# user defined properties')
    }
}

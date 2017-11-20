package de.ostfale.beezle

import groovy.util.logging.Slf4j

@Slf4j
class AppConfig {

    static final String USER_PROFILE = System.getenv("USERPROFILE")
    final static String APP_NAME = 'beezle'
    final static String APP_VERSION = '0.1'
    final static String PROPERTY_FILE_NAME = "beezle.properties"
    final static String SSH_USER = 'git'
    final static int SSH_PORT = 22
}

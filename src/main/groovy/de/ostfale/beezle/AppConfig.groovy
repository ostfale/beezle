package de.ostfale.beezle

import groovy.util.logging.Slf4j

@Slf4j
class AppConfig {

    static final String REPO_SERVER = 'fcu-hh-git'

    static final String USER_PROFILE = System.getenv("USERPROFILE")
    final static String APP_NAME = 'beezle'
    static final String SSH_DEFAULT = USER_PROFILE + File.separator + ".ssh/id_rsa.pub"
    static final String PROPERTY_DEFAULT = USER_PROFILE + File.separator + APP_NAME + File.separator + PROPERTY_FILE_NAME
    final static String APP_VERSION = '0.1'
    final static String PROPERTY_FILE_NAME = "beezle.properties"
    final static String SSH_USER = 'git'
    final static int SSH_PORT = 22
}

package de.ostfale.beezle.control

enum UserProperties {

    REPO_PATH('user.repo.path'), BTU_HOST('system.btu.host')

    final String key

    UserProperties(String key) {
        this.key = key
    }
}
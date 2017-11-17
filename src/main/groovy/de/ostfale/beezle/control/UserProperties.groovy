package de.ostfale.beezle.control

enum UserProperties {

    REPO_PATH('user.repo.path')

    final String key

    UserProperties(String key) {
        this.key = key
    }
}
package de.ostfale.beezle.control

enum UserProperties {

    REPO_PATH('user.repo.path'), SSH_KEY_LOCATION('user.ssh.keyfile'), SSH_PASSWORD('user.ssh.password'), BTU_HOST('system.btu.host')

    final String key

    UserProperties(String key) {
        this.key = key
    }
}
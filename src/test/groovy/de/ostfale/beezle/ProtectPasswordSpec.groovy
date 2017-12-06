package de.ostfale.beezle

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title

/**
 *
 * Created by usauerbrei on 06.12.2017
 */
@Title('Unit test for password protection service')
@Subject(ProtectPassword)
class ProtectPasswordSpec extends Specification {

    final static String PW_TEXT = "test_pw"

    def "test encryption of a given password"() {
        when: "Plain text password will be encrypted"
        String encryptedPW = ProtectPassword.encryptPassword(PW_TEXT)
        then: "The password is now encrypted"
        PW_TEXT != encryptedPW
    }

    def "test decryption of a password"() {
        given: "an encrypted password"
        def encryptedPassword = ProtectPassword.encryptPassword(PW_TEXT)
        when: "encrypted password is now decrypted"
        def decryptedPassword = ProtectPassword.decryptPassword(encryptedPassword)
        then: "Original text password is equal to decrypted password"
        decryptedPassword == PW_TEXT
    }
}

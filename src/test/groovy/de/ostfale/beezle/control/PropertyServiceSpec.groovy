package de.ostfale.beezle.control

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title

import java.nio.file.Path
import java.nio.file.Paths

@Title('Unit test for property service')
@Subject(PropertyService)
class PropertyServiceSpec extends Specification {

    final static String TEST_PATH = 'repo_path'
    final static String TEST_NEW_PATH = 'new_repo_path'

    def "test getProperty with valid key"() {
        given: 'A property file'
        Path propFile = Paths.get(ClassLoader.getSystemResource('test.properties').toURI())
        when: 'an existing value is read from the file'
        def result = PropertyService.instance.getProperty(UserProperties.REPO_PATH.key, propFile.toFile())
        then: 'there is a valid result'
        result == TEST_PATH
    }

    def "test getProperty with invalid key"() {
        given: 'A property file'
        Path propFile = Paths.get(ClassLoader.getSystemResource('test.properties').toURI())
        when: 'a not existing value should be read from the file'
        def result = PropertyService.instance.getProperty('unknown', propFile.toFile())
        then: 'the result is null'
        result == null
    }

    def "test setProperty"() {
        given: 'Property file and a new value stored in another property'
        Path propFile = Paths.get(ClassLoader.getSystemResource('test.properties').toURI())
        when: 'value is written to property file'
        PropertyService.instance.setProperty(UserProperties.REPO_PATH.getKey(), TEST_NEW_PATH, propFile.toFile())
        then: 'value could be read from property file'
        def result = PropertyService.instance.getProperty(UserProperties.REPO_PATH.key, propFile.toFile())
        result != null
        result == TEST_NEW_PATH
        PropertyService.instance.setProperty(UserProperties.REPO_PATH.getKey(), TEST_PATH, propFile.toFile())
        def origValue = PropertyService.instance.getProperty(UserProperties.REPO_PATH.key, propFile.toFile())
        origValue != null
        origValue == TEST_PATH
    }
}

package pl.aswit.theatre.business.version

import spock.lang.Specification

class VersionServiceTest extends Specification {
    def versionService = new VersionServiceImpl("1.1.1")

    def "@getVersion"() {
        given: "version number"
        def expectedVersion = "1.1.1"

        when: "version requested"
        def version = versionService.getVersion()

        then: "version returned"
        version.v == expectedVersion
    }
}

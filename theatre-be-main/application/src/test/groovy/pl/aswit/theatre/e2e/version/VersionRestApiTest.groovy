package pl.aswit.theatre.e2e.version

import com.fasterxml.jackson.core.type.TypeReference
import pl.aswit.theatre.rest.model.version.VersionResponse
import pl.aswit.theatre.e2e.clients.VersionRestApiClient
import pl.aswit.theatre.e2e.config.RestApiTestBase

class VersionRestApiTest extends RestApiTestBase<VersionRestApiClient> {
    def "@getVersion"() {
        given: "setup client"
        def client = setupClient(VersionRestApiClient.class)
        //and:
        //    wireMockRuleHYDRA.stubFor(get(urlPathEqualTo("/process-instance/" + processInstanceId)) // FIXME example - to remove
        //        .withQueryParam("login", equalTo(login))
        //        .withQueryParam("include_config", equalTo("true"))
        //        .willReturn(aResponse()
        //            .withStatus(HttpStatus.OK.value())
        //            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        //            .withBody(readJson("data/confirmProcessInstance/confirmProcessInstanceResponse-get-uw.json"))
        //        )
        //    )

        when:
        def versionResponse = client.getVersion().execute()

        then:
        versionResponse.code() == 200
        versionResponse.body() == getResource("/data/version/getVersionResponse.json", new TypeReference<VersionResponse>() {})
    }
}

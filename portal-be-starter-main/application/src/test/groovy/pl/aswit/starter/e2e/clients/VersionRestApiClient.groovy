package pl.aswit.starter.e2e.clients

import pl.aswit.starter.rest.model.version.VersionResponse

import retrofit2.Call
import retrofit2.http.GET

interface VersionRestApiClient {
    @GET("version")
    Call<VersionResponse> getVersion()
}

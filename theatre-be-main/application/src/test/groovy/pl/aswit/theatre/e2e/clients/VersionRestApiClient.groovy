package pl.aswit.theatre.e2e.clients

import pl.aswit.theatre.rest.model.version.VersionResponse

import retrofit2.Call
import retrofit2.http.GET

interface VersionRestApiClient {
    @GET("version")
    Call<VersionResponse> getVersion()
}

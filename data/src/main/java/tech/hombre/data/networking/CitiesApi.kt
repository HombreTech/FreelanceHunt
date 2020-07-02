package tech.hombre.data.networking

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import tech.hombre.data.networking.model.CitiesResponse

interface CitiesApi {

    @GET("cities/{countryId}")
    suspend fun getCities(@Path("countryId") countryId: Int): Response<CitiesResponse>
}
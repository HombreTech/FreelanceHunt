package tech.hombre.data.networking

import retrofit2.Response
import retrofit2.http.GET
import tech.hombre.data.networking.model.CountriesResponse

interface CountriesApi {

    @GET("countries")
    suspend fun getCountries(): Response<CountriesResponse>
}
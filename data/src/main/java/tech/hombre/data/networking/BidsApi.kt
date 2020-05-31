package tech.hombre.data.networking

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url
import tech.hombre.data.networking.model.MyBidsListResponse

interface BidsApi {

    @GET
    suspend fun getMyBids(@Url url: String): Response<MyBidsListResponse>
}
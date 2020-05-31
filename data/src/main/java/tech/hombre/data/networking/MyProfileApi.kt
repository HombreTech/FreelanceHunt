package tech.hombre.data.networking

import retrofit2.Response
import retrofit2.http.GET
import tech.hombre.data.networking.model.MyProfileResponse

interface MyProfileApi {

    @GET("my/profile")
    suspend fun getMyProfile(): Response<MyProfileResponse>
}
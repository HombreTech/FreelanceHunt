package tech.hombre.data.networking

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url
import tech.hombre.data.networking.model.EmployerDetailResponse
import tech.hombre.data.networking.model.EmployerReviewsResponse
import tech.hombre.data.networking.model.EmployersListResponse

interface EmployersApi {

    @GET
    suspend fun getEmployersList(@Url url: String): Response<EmployersListResponse>

    @GET("employers/{profileId}")
    suspend fun getEmployerDetail(@Path("profileId") profileId: Int): Response<EmployerDetailResponse>

    @GET
    suspend fun getEmployerReviews(@Url url: String): Response<EmployerReviewsResponse>
}
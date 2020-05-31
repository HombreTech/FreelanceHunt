package tech.hombre.data.networking

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url
import tech.hombre.data.networking.model.FreelancerDetailResponse
import tech.hombre.data.networking.model.FreelancerReviewsResponse
import tech.hombre.data.networking.model.FreelancersListResponse

interface FreelancersApi {

    @GET
    suspend fun getFreelancersList(@Url url: String): Response<FreelancersListResponse>

    @GET("freelancers/{profileId}")
    suspend fun getFreelancerDetail(@Path("profileId") profileId: Int): Response<FreelancerDetailResponse>

    @GET
    suspend fun getFreelancerReviews(@Url url: String): Response<FreelancerReviewsResponse>
}
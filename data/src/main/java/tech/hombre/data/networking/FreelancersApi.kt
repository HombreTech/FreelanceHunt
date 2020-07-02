package tech.hombre.data.networking

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url
import tech.hombre.data.networking.model.FreelancerDetailResponse
import tech.hombre.data.networking.model.FreelancerReviewsResponse
import tech.hombre.data.networking.model.FreelancersListResponse

interface FreelancersApi {

    @GET("freelancers")
    suspend fun getFreelancersList(
        @Query("page[number]") page: String,
        @Query("filter[skill_id]") skills: String,
        @Query("filter[country_id]") countryId: Int,
        @Query("filter[city_id]") cityId: Int
    ): Response<FreelancersListResponse>

    @GET("freelancers/{profileId}")
    suspend fun getFreelancerDetail(@Path("profileId") profileId: Int): Response<FreelancerDetailResponse>

    @GET
    suspend fun getFreelancerReviews(@Url url: String): Response<FreelancerReviewsResponse>
}
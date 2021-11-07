package tech.hombre.data.networking

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url
import tech.hombre.data.networking.model.ContestCommentsResponse
import tech.hombre.data.networking.model.ContestDetailResponse
import tech.hombre.data.networking.model.ContestsListResponse
import tech.hombre.data.networking.model.MyContestsListResponse

interface ContestsApi {

    @GET("contests")
    suspend fun getContestsList(@Query("page[number]") page: Int, @Query("filter[skill_id]") skills: String?): Response<ContestsListResponse>

    @GET
    suspend fun getMyContestsList(@Url url: String): Response<MyContestsListResponse>

    @GET("contests/{contestId}")
    suspend fun getContestDetail(@Path("contestId") contestId: Int): Response<ContestDetailResponse>

    @GET("contests/{contestId}/comments")
    suspend fun getContestsComments(@Path("contestId") contestId: Int): Response<ContestCommentsResponse>

    /*@GET("contests/{contestId}/comments")
    suspend fun getContestsApplication(@Path("contestId") contestId: Int): Response<ContestApplicationsResponse>*/
}
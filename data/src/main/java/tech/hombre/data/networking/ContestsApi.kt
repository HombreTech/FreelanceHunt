package tech.hombre.data.networking

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url
import tech.hombre.data.networking.model.ContestCommentsResponse
import tech.hombre.data.networking.model.ContestDetailResponse
import tech.hombre.data.networking.model.ContestsListResponse

interface ContestsApi {

    @GET
    suspend fun getContestsList(@Url url: String): Response<ContestsListResponse>

    @GET("contests/{contestId}")
    suspend fun getContestDetail(@Path("contestId") contestId: Int): Response<ContestDetailResponse>

    @GET("contests/{contestId}/comments")
    suspend fun getContestsComments(@Path("contestId") contestId: Int): Response<ContestCommentsResponse>

    /*@GET("contests/{contestId}/comments")
    suspend fun getContestsApplication(@Path("contestId") contestId: Int): Response<ContestApplicationsResponse>*/
}
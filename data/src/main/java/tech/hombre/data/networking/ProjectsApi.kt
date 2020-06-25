package tech.hombre.data.networking

import retrofit2.Response
import retrofit2.http.*
import tech.hombre.data.networking.model.*

interface ProjectsApi {

    @GET
    suspend fun getProjectsList(@Url url: String): Response<ProjectsListResponse>

    @GET
    suspend fun getMyProjectsList(@Url url: String): Response<MyProjectsListResponse>

    @GET
    suspend fun getProjectDetail(@Url url: String): Response<ProjectDetailResponse>

    @GET("projects/{projectId}/bids")
    suspend fun getProjectBids(@Path("projectId") projectId: Int): Response<ProjectBidsResponse>

    @GET("projects/{projectId}/comments")
    suspend fun getProjectComments(@Path("projectId") projectId: Int): Response<ProjectCommentsResponse>

    @Headers("Content-Type: application/json")
    @POST("projects/{projectId}/bids")
    suspend fun addProjectBid(
        @Path("projectId") projectId: Int, @Body data: AddBidBody
    ): Response<ProjectAddBidResponse>
}
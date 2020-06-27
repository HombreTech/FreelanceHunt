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

    @Headers("Content-Type: application/json")
    @POST("projects")
    suspend fun newPublicProject(
        @Body data: NewPublicProjectBody
    ): Response<ProjectDetailResponse>

    @Headers("Content-Type: application/json")
    @POST("projects")
    suspend fun newPersonalProject(
        @Body data: NewPersonalProjectBody
    ): Response<ProjectDetailResponse>

    @Headers("Content-Type: application/json")
    @POST("projects/{projectId}/extend")
    suspend fun extendProject(
        @Path("projectId") projectId: Int, @Body data: ExtendProjectBody
    ): Response<ProjectDetailResponse>

    @Headers("Content-Type: application/json")
    @POST("projects/{projectId}")
    suspend fun updateProject(
        @Path("projectId") projectId: Int, @Body data: UpdateProjectBody
    ): Response<ProjectDetailResponse>

    @Headers("Content-Type: application/json")
    @POST("projects/{projectId}/amend")
    suspend fun amendProject(
        @Path("projectId") projectId: Int, @Body data: AmendProjectBody
    ): Response<ProjectDetailResponse>
}
package tech.hombre.data.networking

import retrofit2.Response
import retrofit2.http.*
import tech.hombre.data.networking.model.*

interface ProjectsApi {

    @GET("projects")
    suspend fun getProjectsList(@Query("page[number]") page: Int, @Query("filter[only_my_skills]") onlyMySkills: Int, @Query("filter[only_for_plus]") onlyForPlus: Int, @Query("filter[skill_id]") skills: String, @Query("filter[employer_id]") employerId: Int? = null): Response<ProjectsListResponse>

    @GET("projects")
    suspend fun getSimpleProjectsList(@Query("page[number]") page: Int, @Query("filter[only_my_skills]") onlyMySkills: Int, @Query("filter[only_for_plus]") onlyForPlus: Int, @Query("filter[skill_id]") skills: String, @Query("filter[employer_id]") employerId: Int? = null): Response<SimpleProjectsListResponse>

    @GET
    suspend fun getMyProjectsList(@Url url: String): Response<MyProjectsListResponse>

    @GET
    suspend fun getProjectDetail(@Url url: String): Response<ProjectDetailResponse>

    @GET("projects/{projectId}/bids")
    suspend fun getProjectBids(@Path("projectId") projectId: Int, @Query("filter[status]") status: String): Response<ProjectBidsResponse>

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

    @POST("projects/{projectId}/close")
    suspend fun closeProject(
        @Path("projectId") projectId: Int
    ): Response<Unit>

    @Headers("Content-Type: application/json")
    @POST("projects/{projectId}/reopen")
    suspend fun reopenProject(
        @Path("projectId") projectId: Int, @Body data: ExtendProjectBody
    ): Response<Unit>
}
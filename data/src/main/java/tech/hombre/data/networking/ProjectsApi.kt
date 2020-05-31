package tech.hombre.data.networking

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url
import tech.hombre.data.networking.model.ProjectBidsResponse
import tech.hombre.data.networking.model.ProjectCommentsResponse
import tech.hombre.data.networking.model.ProjectDetailResponse
import tech.hombre.data.networking.model.ProjectsListResponse

interface ProjectsApi {

    @GET
    suspend fun getProjectsList(@Url url: String): Response<ProjectsListResponse>

    @GET
    suspend fun getProjectDetail(@Url url: String): Response<ProjectDetailResponse>

    @GET("projects/{projectId}/bids")
    suspend fun getProjectBids(@Path("projectId") projectId: Int): Response<ProjectBidsResponse>

    @GET("projects/{projectId}/comments")
    suspend fun getProjectComments(@Path("projectId") projectId: Int): Response<ProjectCommentsResponse>
}
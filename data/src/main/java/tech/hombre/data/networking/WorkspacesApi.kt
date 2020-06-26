package tech.hombre.data.networking

import retrofit2.Response
import retrofit2.http.*
import tech.hombre.data.networking.model.*

interface WorkspacesApi {

    @GET
    suspend fun getMyWorkspacesList(@Url url: String): Response<MyWorkspacesListResponse>

    @Headers("Content-Type: application/json")
    @POST("my/workspaces/projects/{workspaceId}/propose-conditions")
    suspend fun proposeConditions(
        @Path("workspaceId") workspaceId: Int, @Body data: ProposeConditionsBody
    ): Response<Unit>

    @POST("my/workspaces/projects/{workspaceId}/accept-conditions")
    suspend fun acceptConditions(
        @Path("workspaceId") workspaceId: Int
    ): Response<Unit>

    @POST("my/workspaces/projects/{workspaceId}/reject-conditions")
    suspend fun rejectConditions(
        @Path("workspaceId") workspaceId: Int
    ): Response<Unit>

    @Headers("Content-Type: application/json")
    @POST("my/workspaces/projects/{workspaceId}/extend")
    suspend fun extend(
        @Path("workspaceId") workspaceId: Int, @Body data: ExtendBody
    ): Response<MyWorkspaceResponse>

    @Headers("Content-Type: application/json")
    @POST("my/workspaces/projects/{workspaceId}/request-arbitrage")
    suspend fun requestArbitrage(
        @Path("workspaceId") workspaceId: Int, @Body data: RequestArbitrageBody
    ): Response<Unit>

    @Headers("Content-Type: application/json")
    @POST("my/workspaces/projects/{workspaceId}/complete")
    suspend fun complete(
        @Path("workspaceId") workspaceId: Int, @Body data: CompleteBody
    ): Response<Unit>

    @Headers("Content-Type: application/json")
    @POST("my/workspaces/projects/{workspaceId}/incomplete")
    suspend fun incomplete(
        @Path("workspaceId") workspaceId: Int, @Body data: CompleteBody
    ): Response<Unit>

    @Headers("Content-Type: application/json")
    @POST("my/workspaces/projects/{workspaceId}/close")
    suspend fun close(
        @Path("workspaceId") workspaceId: Int, @Body data: CloseBody
    ): Response<Unit>

    @Headers("Content-Type: application/json")
    @POST("my/workspaces/projects/{workspaceId}/review")
    suspend fun review(
        @Path("workspaceId") workspaceId: Int, @Body data: ReviewBody
    ): Response<Unit>
}
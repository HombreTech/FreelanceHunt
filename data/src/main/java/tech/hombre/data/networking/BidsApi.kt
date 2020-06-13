package tech.hombre.data.networking

import retrofit2.Response
import retrofit2.http.*
import tech.hombre.data.networking.model.MyBidsListResponse
import tech.hombre.data.networking.model.ProjectAddBidResponse

interface BidsApi {

    @GET
    suspend fun getMyBids(@Url url: String): Response<MyBidsListResponse>

    @POST("projects/{projectId}/bids/{bidId}/revoke")
    suspend fun revokeProjectBid(
        @Path("projectId") projectId: Int, @Path("bidId") bidId: Int
    ): Response<ProjectAddBidResponse>

    @POST("projects/{projectId}/bids/{bidId}/reject")
    suspend fun rejectProjectBid(
        @Path("projectId") projectId: Int, @Path("bidId") bidId: Int
    ): Response<ProjectAddBidResponse>

    @POST("projects/{projectId}/bids/{bidId}/choose")
    suspend fun chooseWinnerProjectBid(
        @Path("projectId") projectId: Int, @Path("bidId") bidId: Int, @Body comment: String
    ): Response<ProjectAddBidResponse>
}
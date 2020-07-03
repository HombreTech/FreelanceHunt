package tech.hombre.data.networking

import retrofit2.Response
import retrofit2.http.*
import tech.hombre.data.networking.model.ChooseBidBody
import tech.hombre.data.networking.model.MyBidsListResponse

interface BidsApi {

    @GET("my/bids")
    suspend fun getMyBids(@Query("page[number]") page: Int, @Query("filter[status]") status: String): Response<MyBidsListResponse>

    @POST("projects/{projectId}/bids/{bidId}/revoke")
    suspend fun revokeProjectBid(
        @Path("projectId") projectId: Int, @Path("bidId") bidId: Int
    ): Response<Unit>

    @POST("projects/{projectId}/bids/{bidId}/reject")
    suspend fun rejectProjectBid(
        @Path("projectId") projectId: Int, @Path("bidId") bidId: Int
    ): Response<Unit>

    @POST("projects/{projectId}/bids/{bidId}/choose")
    suspend fun chooseWinnerProjectBid(
        @Path("projectId") projectId: Int, @Path("bidId") bidId: Int, @Body data: ChooseBidBody
    ): Response<Unit>
}
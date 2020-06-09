package tech.hombre.data.networking

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url
import tech.hombre.data.networking.model.FeedListResponse
import tech.hombre.data.networking.model.FeedListSimpleResponse

interface FeedApi {

    @GET
    suspend fun getFeedList(@Url url: String): Response<FeedListResponse>

    @GET("my/feed")
    suspend fun getFeedSimpleList(): Response<FeedListSimpleResponse>

    @POST("my/feed/read")
    suspend fun markFeedAsRead(): Response<Unit>
}
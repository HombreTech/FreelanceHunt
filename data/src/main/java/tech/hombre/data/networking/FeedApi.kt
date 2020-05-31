package tech.hombre.data.networking

import retrofit2.Response
import retrofit2.http.GET
import tech.hombre.data.networking.model.FeedListResponse

interface FeedApi {

    @GET("my/feed")
    suspend fun getFeedList(): Response<FeedListResponse>

    @GET("my/feed/read")
    suspend fun markFeedAsRead(): Response<Any>
}
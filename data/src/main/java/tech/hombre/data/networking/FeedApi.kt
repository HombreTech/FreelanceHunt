package tech.hombre.data.networking

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url
import tech.hombre.data.networking.model.FeedListResponse

interface FeedApi {

    @GET
    suspend fun getFeedList(@Url url: String): Response<FeedListResponse>

    @GET("my/feed/read")
    suspend fun markFeedAsRead(): Response<Any>
}
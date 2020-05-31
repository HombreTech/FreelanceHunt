package tech.hombre.data.networking

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url
import tech.hombre.data.networking.model.ThreadMessageListResponse
import tech.hombre.data.networking.model.ThreadsListResponse

interface ThreadsApi {

    @GET
    suspend fun getThreadsList(@Url url: String): Response<ThreadsListResponse>

    @GET("threads/{threadId}")
    suspend fun getThreadMessagesList(@Path("threadId") threadId: Int): Response<ThreadMessageListResponse>
}
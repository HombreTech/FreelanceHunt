package tech.hombre.data.networking

import retrofit2.Response
import retrofit2.http.*
import tech.hombre.data.networking.model.*

interface ThreadsApi {

    @GET
    suspend fun getThreadsList(@Url url: String): Response<ThreadsListResponse>

    @GET("threads/{threadId}")
    suspend fun getThreadMessagesList(@Path("threadId") threadId: Int): Response<ThreadMessageListResponse>

    @POST("threads/{threadId}")
    suspend fun sendThreadMessage(@Path("threadId") threadId: Int, @Body data: SendMessageBody): Response<SendThreadMessageResponse>

    @POST("threads")
    suspend fun createThreadMessage(@Body data: CreateThreadBody): Response<CreateThreadMessageResponse>

    @POST("threads/action/support")
    suspend fun sendSupportMessage(@Body data: SendSupportMessageBody): Response<SendSupportMessageResponse>
}
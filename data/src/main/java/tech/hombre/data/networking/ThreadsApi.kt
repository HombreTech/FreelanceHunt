package tech.hombre.data.networking

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import tech.hombre.data.networking.model.*
import tech.hombre.domain.model.UploadedThreadMessage

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

    @Multipart
    @POST("threads/{threadId}/attachment")
    fun uploadAttach(@Path("threadId") threadId: Int, @Part file: MultipartBody.Part): Call<UploadedThreadMessage>
}
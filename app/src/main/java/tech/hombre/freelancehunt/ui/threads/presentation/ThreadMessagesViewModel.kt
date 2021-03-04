package tech.hombre.freelancehunt.ui.threads.presentation

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tech.hombre.data.networking.ThreadsApi
import tech.hombre.data.networking.model.RequestBodyWithProgress
import tech.hombre.domain.interaction.threadslist.messages.GetThreadMessageListUseCase
import tech.hombre.domain.interaction.threadslist.messages.SendThreadMessageUseCase
import tech.hombre.domain.model.ThreadMessageList
import tech.hombre.domain.model.UploadedThreadMessage
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.App
import tech.hombre.freelancehunt.common.extensions.copyStreamToFile
import tech.hombre.freelancehunt.common.extensions.extension
import tech.hombre.freelancehunt.common.extensions.getMimeType
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success
import tech.hombre.freelancehunt.ui.base.ViewState
import java.io.File
import java.net.URLEncoder


class ThreadMessagesViewModel(
    private val getThreadMessageList: GetThreadMessageListUseCase,
    private val sendThreadMessage: SendThreadMessageUseCase,
    private val threadsApi: ThreadsApi
) :
    BaseViewModel<ThreadMessageList>() {

    val _message = MutableLiveData<ViewState<ThreadMessageList.Data>>()
    val message: LiveData<ViewState<ThreadMessageList.Data>>
        get() = _message

    val _uploading = MutableLiveData<ViewState<ThreadMessageList.Data>>()
    val uploading: LiveData<ViewState<ThreadMessageList.Data>>
        get() = _uploading

    fun getMessages(threadId: Int) = executeUseCase {
        getThreadMessageList(threadId)
            .onSuccess { _viewState.value = Success(it) }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun sendMessage(threadId: Int, message: String) = executeUseCase {
        sendThreadMessage(threadId, message)
            .onSuccess { _message.value = Success(it) }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun uploadAttach(
        threadId: Int,
        path: String,
        filename: String,
        progressCallback: ((progress: Float) -> Unit)?
    ) {
        val file = File(path)
        val requestFile = RequestBodyWithProgress(
            file,
            getMimeType(path),
            progressCallback
        )
        val body = MultipartBody.Part.createFormData(URLEncoder.encode(filename, "utf-8"), URLEncoder.encode(file.name, "utf-8"), requestFile)
        threadsApi.uploadAttach(threadId, body).enqueue(object : Callback<UploadedThreadMessage> {
            override fun onFailure(call: Call<UploadedThreadMessage>, t: Throwable) {
                _uploading.value = Error(t)
            }

            override fun onResponse(
                call: Call<UploadedThreadMessage>,
                response: Response<UploadedThreadMessage>
            ) {
                response.body()?.let { _uploading.value = Success(it.data) }
            }
        })

    }

    fun uploadAttach(
        uri: Uri,
        threadId: Int,
        filename: String,
        progressCallback: ((progress: Float) -> Unit)?
    ) {
        App.instance.contentResolver.openInputStream(uri)?.use { inputStream ->
            val tempFile = File.createTempFile("attach","." + filename.extension())

            copyStreamToFile(inputStream, tempFile)

            val filePath = tempFile.absolutePath

            val file = File(filePath)

            val requestFile = RequestBodyWithProgress(
                file,
                getMimeType(filePath),
                progressCallback
            )
            val body = MultipartBody.Part.createFormData(filename, filename, requestFile)

            threadsApi.uploadAttach(threadId, body).enqueue(object : Callback<UploadedThreadMessage> {
                override fun onFailure(call: Call<UploadedThreadMessage>, t: Throwable) {
                    _uploading.value = Error(t)
                    file.delete()
                }

                override fun onResponse(
                    call: Call<UploadedThreadMessage>,
                    response: Response<UploadedThreadMessage>
                ) {
                    response.body()?.let { _uploading.value = Success(it.data) }
                    file.delete()
                }
            })
        }


    }


}

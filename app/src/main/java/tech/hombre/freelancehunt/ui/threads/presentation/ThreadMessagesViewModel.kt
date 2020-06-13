package tech.hombre.freelancehunt.ui.threads.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tech.hombre.domain.interaction.threadslist.messages.GetThreadMessageListUseCase
import tech.hombre.domain.interaction.threadslist.messages.SendThreadMessageUseCase
import tech.hombre.domain.model.ThreadMessageList
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success
import tech.hombre.freelancehunt.ui.base.ViewState

class ThreadMessagesViewModel(
    private val getThreadMessageList: GetThreadMessageListUseCase,
    private val sendThreadMessage: SendThreadMessageUseCase
) :
    BaseViewModel<ThreadMessageList>() {

    val _message = MutableLiveData<ViewState<ThreadMessageList.Data>>()
    val message: LiveData<ViewState<ThreadMessageList.Data>>
        get() = _message

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
}

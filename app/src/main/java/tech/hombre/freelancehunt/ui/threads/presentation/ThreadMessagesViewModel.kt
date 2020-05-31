package tech.hombre.freelancehunt.ui.threads.presentation

import tech.hombre.domain.interaction.threadslist.messages.GetThreadMessageListUseCase
import tech.hombre.domain.model.ThreadMessageList
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success

class ThreadMessagesViewModel(private val getThreadMessageList: GetThreadMessageListUseCase) :
    BaseViewModel<ThreadMessageList>() {

    fun getMessages(threadId: Int) = executeUseCase {
        getThreadMessageList(threadId)
            .onSuccess { _viewState.value = Success(it) }
            .onFailure { _viewState.value = Error(it.throwable) }
    }
}

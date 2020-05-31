package tech.hombre.freelancehunt.ui.threads.presentation

import tech.hombre.domain.interaction.threadslist.GetThreadsListUseCase
import tech.hombre.domain.model.ThreadsList
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success

class ThreadsViewModel(private val getThreadsList: GetThreadsListUseCase) :
    BaseViewModel<ThreadsList>() {

    lateinit var pagination: ThreadsList.Links

    fun getThreads(url: String = "threads") = executeUseCase {
        getThreadsList(url)
            .onSuccess {
                pagination = it.links
                _viewState.value = Success(it)
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }
}

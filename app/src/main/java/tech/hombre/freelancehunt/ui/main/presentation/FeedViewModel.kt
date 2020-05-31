package tech.hombre.freelancehunt.ui.main.presentation

import tech.hombre.domain.interaction.feedlist.GetFeedListUseCase
import tech.hombre.domain.model.FeedList
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success

class FeedViewModel(private val getFeedList: GetFeedListUseCase) :
    BaseViewModel<FeedList>() {

    fun getFeedLists(param: String = "") = executeUseCase {
        getFeedList(param)
            .onSuccess { _viewState.value = Success(it) }
            .onFailure { _viewState.value = Error(it.throwable) }
    }
}

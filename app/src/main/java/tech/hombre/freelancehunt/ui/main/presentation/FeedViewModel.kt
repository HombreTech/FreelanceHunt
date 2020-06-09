package tech.hombre.freelancehunt.ui.main.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tech.hombre.data.BuildConfig
import tech.hombre.domain.interaction.feedlist.GetFeedListUseCase
import tech.hombre.domain.interaction.feedlist.MarkFeedAsReadUseCase
import tech.hombre.domain.interaction.projectslist.detail.GetProjectDetailUseCase
import tech.hombre.domain.model.*
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success
import tech.hombre.freelancehunt.ui.base.ViewState

class FeedViewModel(
    private val getFeedList: GetFeedListUseCase,
    private val getProjectDetail: GetProjectDetailUseCase,
    private val markFeedAsRead: MarkFeedAsReadUseCase
) :
    BaseViewModel<FeedList>() {

    val _details = MutableLiveData<ViewState<ProjectDetail>>()
    val details: LiveData<ViewState<ProjectDetail>>
        get() = _details

    val _feedMark = MutableLiveData<ViewState<Boolean>>()
    val feedMarked: LiveData<ViewState<Boolean>>
        get() = _feedMark

    fun getFeedLists(url: String = BuildConfig.BASE_URL + "my/feed") = executeUseCase {
        getFeedList(url)
            .onSuccess { _viewState.value = Success(it) }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun getProjectDetails(url: String) = executeUseCase {
        getProjectDetail(url)
            .onSuccess { _details.value = Success(it) }
            .onFailure { _details.value = Error(it.throwable) }
    }

    fun markFeedAsReaded() = executeUseCase {
        markFeedAsRead()
            .onSuccess { _feedMark.value = Success(it) }
            .onFailure { _feedMark.value = Error(it.throwable) }
    }

}

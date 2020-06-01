package tech.hombre.freelancehunt.ui.main.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tech.hombre.domain.interaction.feedlist.GetFeedListUseCase
import tech.hombre.domain.interaction.projectslist.detail.GetProjectDetailUseCase
import tech.hombre.domain.model.FeedList
import tech.hombre.domain.model.ProjectDetail
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success
import tech.hombre.freelancehunt.ui.base.ViewState

class FeedViewModel(private val getFeedList: GetFeedListUseCase, private val getProjectDetail: GetProjectDetailUseCase) :
    BaseViewModel<FeedList>() {

    val _details = MutableLiveData<ViewState<ProjectDetail>>()
    val details: LiveData<ViewState<ProjectDetail>>
        get() = _details

    fun getFeedLists(param: String = "") = executeUseCase {
        getFeedList(param)
            .onSuccess { _viewState.value = Success(it) }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun getProjectDetails(url: String) = executeUseCase {
        getProjectDetail(url)
            .onSuccess { _details.value = Success(it) }
            .onFailure { _details.value = Error(it.throwable) }
    }
}

package tech.hombre.freelancehunt.ui.my.bids.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tech.hombre.domain.interaction.mybids.GetMyBidsListUseCase
import tech.hombre.domain.interaction.projectslist.detail.GetProjectDetailUseCase
import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.ProjectDetail
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success
import tech.hombre.freelancehunt.ui.base.ViewState

class MyBidsViewModel(private val getMyBidsList: GetMyBidsListUseCase,
                      private val getProjectDetail: GetProjectDetailUseCase) :
    BaseViewModel<MyBidsList>() {

    lateinit var pagination: MyBidsList.Links

    val _projectDetails = MutableLiveData<ViewState<ProjectDetail>>()
    val projectDetails: LiveData<ViewState<ProjectDetail>>
        get() = _projectDetails

    fun getMyBids(url: String = "my/bids") = executeUseCase {
        getMyBidsList(url)
            .onSuccess {
                pagination = it.links
                _viewState.value = Success(it)
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun getProjectDetails(url: String) = executeUseCase {
        getProjectDetail(url)
            .onSuccess { _projectDetails.value = Success(it) }
            .onFailure { _projectDetails.value = Error(it.throwable) }
    }
}

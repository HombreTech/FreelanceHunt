package tech.hombre.freelancehunt.ui.my.bids.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tech.hombre.domain.interaction.mybids.GetMyBidsListUseCase
import tech.hombre.domain.interaction.projectslist.bids.RevokeProjectBidsUseCase
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
                      private val getProjectDetail: GetProjectDetailUseCase,
                      private val revokeProjectBid: RevokeProjectBidsUseCase) :
    BaseViewModel<MyBidsList>() {

    lateinit var pagination: MyBidsList.Links

    val _projectDetails = MutableLiveData<ViewState<ProjectDetail>>()
    val projectDetails: LiveData<ViewState<ProjectDetail>>
        get() = _projectDetails

    val _bidAction = MutableLiveData<ViewState<Pair<Int, String>>>()
    val bidAction: LiveData<ViewState<Pair<Int, String>>>
        get() = _bidAction

    fun getMyBids(page: Int, status: String = "active") = executeUseCase {
        getMyBidsList(page, status)
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

    fun revokeProjectBids(projectId: Int, bidId: Int) = executeUseCase{
        revokeProjectBid(projectId, bidId)
            .onSuccess {
                _bidAction.value = Success(Pair(bidId, "revoked"))
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }
}

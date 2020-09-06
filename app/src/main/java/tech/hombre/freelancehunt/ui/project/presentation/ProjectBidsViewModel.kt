package tech.hombre.freelancehunt.ui.project.presentation


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tech.hombre.domain.interaction.projectslist.bids.ChooseProjectBidsUseCase
import tech.hombre.domain.interaction.projectslist.bids.GetProjectBidsUseCase
import tech.hombre.domain.interaction.projectslist.bids.RejectProjectBidsUseCase
import tech.hombre.domain.interaction.projectslist.bids.RevokeProjectBidsUseCase
import tech.hombre.domain.model.ProjectBid
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success
import tech.hombre.freelancehunt.ui.base.ViewState

class ProjectBidsViewModel(
    private val getProjectBid: GetProjectBidsUseCase,
    private val revokeProjectBid: RevokeProjectBidsUseCase,
    private val rejectProjectBid: RejectProjectBidsUseCase,
    private val chooseProjectBid: ChooseProjectBidsUseCase
) :
    BaseViewModel<ProjectBid>() {

    val _bidAction = MutableLiveData<ViewState<Pair<Int, String>>>()
    val bidAction: LiveData<ViewState<Pair<Int, String>>>
        get() = _bidAction

    fun getProjectBids(projectId: Int, status: String = "active") = executeUseCase {
        getProjectBid(projectId, status)
            .onSuccess {
                _viewState.value = Success(it)
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun revokeProjectBids(projectId: Int, bidId: Int) = executeUseCase{
        revokeProjectBid(projectId, bidId)
            .onSuccess {
                _bidAction.value = Success(Pair(bidId, "revoked"))
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun rejectProjectBids(projectId: Int, bidId: Int) = executeUseCase{
        rejectProjectBid(projectId, bidId)
            .onSuccess {
                _bidAction.value = Success(Pair(bidId, "rejected"))
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun chooseProjectBids(projectId: Int, bidId: Int, comment: String) = executeUseCase{
        chooseProjectBid(projectId, bidId, comment)
            .onSuccess {
                _bidAction.value = Success(Pair(bidId, "choose"))
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }
}

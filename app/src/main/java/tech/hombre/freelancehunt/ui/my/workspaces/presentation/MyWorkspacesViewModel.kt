package tech.hombre.freelancehunt.ui.my.workspaces.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tech.hombre.data.BuildConfig
import tech.hombre.domain.interaction.myworkspaces.*
import tech.hombre.domain.model.*
import tech.hombre.freelancehunt.common.SafeType
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success
import tech.hombre.freelancehunt.ui.base.ViewState

class MyWorkspacesViewModel(
    private val getMyWorkspacesList: GetMyWorkspacesListUseCase,
    private val proposeConditions: ProposeConditionsUseCase,
    private val acceptConditions: AcceptConditionsUseCase,
    private val rejectConditions: RejectConditionsUseCase,
    private val extendConditions: ExtendConditionsUseCase,
    private val requestArbitrage: RequestArbitrageUseCase,
    private val closeWorkspace: WorkspaceCloseUseCase,
    private val completeWorkspace: WorkspaceCompleteUseCase,
    private val incompleteWorkspace: WorkspaceIncompleteUseCase,
    private val reviewWorkspace: WorkspaceReviewUseCase
) :
    BaseViewModel<WorkspacesList>() {

    lateinit var pagination: WorkspacesList.Links

    val _workspaceAction = MutableLiveData<ViewState<Pair<Int, String>>>()
    val workspaceAction: LiveData<ViewState<Pair<Int, String>>>
        get() = _workspaceAction

    fun getMyWorkspacesLists(url: String = BuildConfig.BASE_URL + "my/workspaces/projects") =
        executeUseCase {
            getMyWorkspacesList(url)
                .onSuccess {
                    pagination = it.links
                    _viewState.value = Success(it)
                }
                .onFailure { _viewState.value = Error(it.throwable) }
        }

    fun proposeCondition(
        workspaceId: Int,
        days: Int,
        budget: MyBidsList.Data.Attributes.Budget,
        safeType: SafeType,
        comment: String
    ) = executeUseCase {
        proposeConditions(workspaceId, days, budget, safeType.type!!, comment)
            .onSuccess {
                _workspaceAction.value = Success(Pair(workspaceId, "propose"))
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun acceptCondition(workspaceId: Int) = executeUseCase {
        acceptConditions(workspaceId)
            .onSuccess {
                _workspaceAction.value = Success(Pair(workspaceId, "accept"))
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun rejectCondition(workspaceId: Int) = executeUseCase {
        rejectConditions(workspaceId)
            .onSuccess {
                _workspaceAction.value = Success(Pair(workspaceId, "reject"))
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun extendCondition(workspaceId: Int, days: Int) = executeUseCase {
        extendConditions(workspaceId, days)
            .onSuccess {
                _workspaceAction.value = Success(Pair(workspaceId, "extend"))
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun requestArbitrages(workspaceId: Int, comment: String) = executeUseCase {
        requestArbitrage(workspaceId, comment)
            .onSuccess {
                _workspaceAction.value = Success(Pair(workspaceId, "arbitrage"))
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun closeWorkspaces(workspaceId: Int, review: String) = executeUseCase {
        closeWorkspace(workspaceId, review)
            .onSuccess {
                _workspaceAction.value = Success(Pair(workspaceId, "close_without_review"))
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun completeWorkspaces(workspaceId: Int, grades: CompleteGrades, review: String) = executeUseCase {
        completeWorkspace(workspaceId, grades, review)
            .onSuccess {
                _workspaceAction.value = Success(Pair(workspaceId, "complete"))
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun incompleteWorkspaces(workspaceId: Int, grades: CompleteGrades, review: String) = executeUseCase {
        incompleteWorkspace(workspaceId, grades, review)
            .onSuccess {
                _workspaceAction.value = Success(Pair(workspaceId, "incomplete"))
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun reviewWorkspaces(workspaceId: Int, grades: ReviewGrades, review: String) = executeUseCase {
        reviewWorkspace(workspaceId, grades, review)
            .onSuccess {
                _workspaceAction.value = Success(Pair(workspaceId, "write_review"))
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

}

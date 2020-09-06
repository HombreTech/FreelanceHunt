package tech.hombre.freelancehunt.ui.menu.model


import tech.hombre.domain.interaction.projectslist.bids.AddProjectBidsUseCase
import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.ProjectBid
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.common.SafeType
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success

class AddBidsViewModel(
    private val addProjectBid: AddProjectBidsUseCase
) :
    BaseViewModel<ProjectBid.Data>() {

    fun addNewProjectBid(
        projectId: Int,
        days: Int,
        budget: MyBidsList.Data.Attributes.Budget,
        safeType: SafeType,
        comment: String,
        isHidden: Boolean
    ) = executeUseCase {
        addProjectBid(projectId, days, budget, safeType.type!!, comment, isHidden)
            .onSuccess {
                _viewState.value = Success(it)
            }
            .onFailure {
                _viewState.value = Error(it.throwable)
            }
    }

}

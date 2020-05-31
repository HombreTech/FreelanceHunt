package tech.hombre.freelancehunt.ui.project.presentation


import tech.hombre.domain.interaction.projectslist.bids.GetProjectBidsUseCase
import tech.hombre.domain.model.ProjectBid
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success

class ProjectBidsViewModel(private val getProjectBid: GetProjectBidsUseCase) :
    BaseViewModel<ProjectBid>() {

    fun getProjectBids(projectId: Int) = executeUseCase {
        getProjectBid(projectId)
            .onSuccess {
                _viewState.value = Success(it)
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }
}

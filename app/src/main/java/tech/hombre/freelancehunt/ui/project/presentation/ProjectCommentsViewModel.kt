package tech.hombre.freelancehunt.ui.project.presentation

import tech.hombre.domain.interaction.projectslist.comments.GetProjectCommentsUseCase
import tech.hombre.domain.model.ProjectComment
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success

class ProjectCommentsViewModel(private val getProjectComments: GetProjectCommentsUseCase) :
    BaseViewModel<ProjectComment>() {

    fun getProjectComment(projectId: Int) = executeUseCase {
        getProjectComments(projectId)
            .onSuccess {
                _viewState.value = Success(it)
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }
}

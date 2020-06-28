package tech.hombre.freelancehunt.ui.employers.presentation

import tech.hombre.domain.interaction.projectslist.GetProjectsListUseCase
import tech.hombre.domain.model.ProjectsList
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success

class EmployerProjectsViewModel(
    private val getProjectsList: GetProjectsListUseCase
) :
    BaseViewModel<ProjectsList>() {

    lateinit var pagination: ProjectsList.Links


    fun getProjectsLists(page: Int, employerId: Int) = executeUseCase {
        getProjectsList(page.toString(), false, false, "", employerId)
            .onSuccess {
                pagination = it.links

                _viewState.value = Success(it)
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }


}

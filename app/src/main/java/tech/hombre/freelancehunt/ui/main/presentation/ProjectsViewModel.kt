package tech.hombre.freelancehunt.ui.main.presentation

import tech.hombre.data.BuildConfig
import tech.hombre.domain.interaction.projectslist.GetProjectsListUseCase
import tech.hombre.domain.model.ProjectsList
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success

class ProjectsViewModel(private val getProjectsList: GetProjectsListUseCase) :
    BaseViewModel<ProjectsList>() {

    lateinit var pagination: ProjectsList.Links

    fun getProjectsLists(url: String = BuildConfig.BASE_URL + "projects") = executeUseCase {
        getProjectsList(url)
            .onSuccess {
                pagination = it.links
                _viewState.value = Success(it)
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }
}

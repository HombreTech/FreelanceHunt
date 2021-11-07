package tech.hombre.freelancehunt.ui.main.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tech.hombre.domain.interaction.projectslist.GetProjectsListUseCase
import tech.hombre.domain.interaction.projectslist.detail.GetProjectDetailUseCase
import tech.hombre.domain.model.ProjectDetail
import tech.hombre.domain.model.ProjectsList
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success
import tech.hombre.freelancehunt.ui.base.ViewState

class ProjectsViewModel(
    private val getProjectsList: GetProjectsListUseCase,
    private val getProjectDetail: GetProjectDetailUseCase
) :
    BaseViewModel<ProjectsList>() {

    lateinit var pagination: ProjectsList.Links

    var skills = intArrayOf()
    var onlyMySkills = false
    var onlyForPlus = false

    val _details = MutableLiveData<ViewState<ProjectDetail>>()
    val details: LiveData<ViewState<ProjectDetail>>
        get() = _details

    fun getProjectsLists(page: Int) = executeUseCase {
        getProjectsList(page, onlyMySkills, onlyForPlus, skills.joinToString(separator = ",") { it.toString() }, null)
            .onSuccess {
                pagination = it.links
                _viewState.value = Success(it)
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun getProjectDetails(url: String) = executeUseCase {
        getProjectDetail(url)
            .onSuccess { _details.value = Success(it) }
            .onFailure { _details.value = Error(it.throwable) }
    }

    fun setProjectFilters(
        onlyMySkills: Boolean,
        skills: IntArray,
        onlyForPlus: Boolean
    ) {
        this.skills = skills
        this.onlyMySkills = onlyMySkills
        this.onlyForPlus = onlyForPlus
    }
}

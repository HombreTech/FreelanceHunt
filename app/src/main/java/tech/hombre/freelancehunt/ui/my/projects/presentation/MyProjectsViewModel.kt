package tech.hombre.freelancehunt.ui.my.projects.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tech.hombre.data.BuildConfig
import tech.hombre.domain.interaction.myprojects.GetMyProjectsListUseCase
import tech.hombre.domain.interaction.projectslist.ExtendProjectUseCase
import tech.hombre.domain.interaction.projectslist.detail.GetProjectDetailUseCase
import tech.hombre.domain.model.MyProjectsList
import tech.hombre.domain.model.ProjectDetail
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success
import tech.hombre.freelancehunt.ui.base.ViewState

class MyProjectsViewModel(
    private val getMyContestsList: GetMyProjectsListUseCase,
    private val getProjectDetail: GetProjectDetailUseCase,
    private val extendProject: ExtendProjectUseCase
) :
    BaseViewModel<MyProjectsList>() {

    lateinit var pagination: MyProjectsList.Links

    val _details = MutableLiveData<ViewState<ProjectDetail>>()
    val details: LiveData<ViewState<ProjectDetail>>
        get() = _details

    val _action = MutableLiveData<ViewState<Pair<Int, ProjectDetail>>>()
    val action: LiveData<ViewState<Pair<Int, ProjectDetail>>>
        get() = _action

    fun getMyProjectsLists(url: String = BuildConfig.BASE_URL + "my/projects") = executeUseCase {
        getMyContestsList(url)
            .onSuccess {
                pagination = it.links
                _viewState.value = Success(it)
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun getContestDetails(url: String) = executeUseCase {
        getProjectDetail(url)
            .onSuccess {
                _details.value = Success(it)
            }
            .onFailure { _details.value = Error(it.throwable) }
    }

    fun extendProjects(projectId: Int, expiredAt: String) = executeUseCase {
        extendProject(projectId, expiredAt)
            .onSuccess {
                _action.value = Success(Pair(projectId, it))
            }
            .onFailure { _action.value = Error(it.throwable) }
    }
}

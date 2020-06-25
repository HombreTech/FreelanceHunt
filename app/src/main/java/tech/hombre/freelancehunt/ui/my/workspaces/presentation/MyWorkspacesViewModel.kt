package tech.hombre.freelancehunt.ui.my.workspaces.presentation

import tech.hombre.data.BuildConfig
import tech.hombre.domain.interaction.myworkspaces.GetMyWorkspacesListUseCase
import tech.hombre.domain.model.WorkspacesList
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success

class MyWorkspacesViewModel(
    private val getMyWorkspacesList: GetMyWorkspacesListUseCase
) :
    BaseViewModel<WorkspacesList>() {

    lateinit var pagination: WorkspacesList.Links

    fun getMyWorkspacesLists(url: String = BuildConfig.BASE_URL + "my/workspaces/projects") = executeUseCase {
        getMyWorkspacesList(url)
            .onSuccess {
                pagination = it.links
                _viewState.value = Success(it)
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

}

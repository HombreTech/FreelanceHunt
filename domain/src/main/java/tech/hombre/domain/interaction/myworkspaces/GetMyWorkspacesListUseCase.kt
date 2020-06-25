package tech.hombre.domain.interaction.myworkspaces

import tech.hombre.domain.base.BaseUseCase
import tech.hombre.domain.model.MyProjectsList
import tech.hombre.domain.model.Result
import tech.hombre.domain.model.WorkspacesList

interface GetMyWorkspacesListUseCase : BaseUseCase<String, WorkspacesList> {

    override suspend operator fun invoke(url: String): Result<WorkspacesList>
}
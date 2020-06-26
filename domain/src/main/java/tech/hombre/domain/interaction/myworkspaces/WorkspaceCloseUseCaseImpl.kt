package tech.hombre.domain.interaction.myworkspaces

import tech.hombre.domain.repository.WorkspaceCloseRepository

class WorkspaceCloseUseCaseImpl(private val workspaceslistRepository: WorkspaceCloseRepository) :
    WorkspaceCloseUseCase {

    override suspend operator fun invoke(
        workspaceId: Int,
        review: String
    ) = workspaceslistRepository.closeWorkspace(workspaceId, review)
}
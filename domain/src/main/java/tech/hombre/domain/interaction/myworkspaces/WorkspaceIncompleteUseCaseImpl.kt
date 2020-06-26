package tech.hombre.domain.interaction.myworkspaces

import tech.hombre.domain.model.CompleteGrades
import tech.hombre.domain.repository.WorkspaceIncompleteRepository

class WorkspaceIncompleteUseCaseImpl(private val workspaceslistRepository: WorkspaceIncompleteRepository) :
    WorkspaceIncompleteUseCase {

    override suspend operator fun invoke(
        workspaceId: Int,
        grades: CompleteGrades,
        review: String
    ) = workspaceslistRepository.incompleteWorkspace(workspaceId, grades, review)
}
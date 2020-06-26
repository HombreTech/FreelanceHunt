package tech.hombre.domain.interaction.myworkspaces

import tech.hombre.domain.model.CompleteGrades
import tech.hombre.domain.repository.WorkspaceCompleteRepository

class WorkspaceCompleteUseCaseImpl(private val workspaceslistRepository: WorkspaceCompleteRepository) :
    WorkspaceCompleteUseCase {

    override suspend operator fun invoke(
        workspaceId: Int,
        grades: CompleteGrades,
        review: String
    ) = workspaceslistRepository.completeWorkspace(workspaceId, grades, review)
}
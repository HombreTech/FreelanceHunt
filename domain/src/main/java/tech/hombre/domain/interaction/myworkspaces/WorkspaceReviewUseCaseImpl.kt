package tech.hombre.domain.interaction.myworkspaces

import tech.hombre.domain.model.ReviewGrades
import tech.hombre.domain.repository.WorkspaceReviewRepository

class WorkspaceReviewUseCaseImpl(private val workspaceslistRepository: WorkspaceReviewRepository) :
    WorkspaceReviewUseCase {

    override suspend operator fun invoke(
        workspaceId: Int,
        grades: ReviewGrades,
        review: String
    ) = workspaceslistRepository.reviewWorkspace(workspaceId, grades, review)
}
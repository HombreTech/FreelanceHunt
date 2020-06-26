package tech.hombre.domain.interaction.myworkspaces

import tech.hombre.domain.model.Result
import tech.hombre.domain.model.ReviewGrades

interface WorkspaceReviewUseCase {

    suspend operator fun invoke(
        workspaceId: Int,
        grades: ReviewGrades,
        review: String
    ): Result<Boolean>
}
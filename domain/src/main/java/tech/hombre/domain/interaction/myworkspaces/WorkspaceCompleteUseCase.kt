package tech.hombre.domain.interaction.myworkspaces

import tech.hombre.domain.model.CompleteGrades
import tech.hombre.domain.model.Result

interface WorkspaceCompleteUseCase {

    suspend operator fun invoke(
        workspaceId: Int,
        grades: CompleteGrades,
        review: String
    ): Result<Boolean>
}
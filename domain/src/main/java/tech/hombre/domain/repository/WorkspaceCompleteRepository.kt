package tech.hombre.domain.repository

import tech.hombre.domain.model.CompleteGrades
import tech.hombre.domain.model.Result

interface WorkspaceCompleteRepository {

    suspend fun completeWorkspace(
        workspaceId: Int,
        grades: CompleteGrades,
        review: String
    ): Result<Boolean>

}
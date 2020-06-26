package tech.hombre.domain.repository

import tech.hombre.domain.model.Result
import tech.hombre.domain.model.ReviewGrades

interface WorkspaceReviewRepository {

    suspend fun reviewWorkspace(
        workspaceId: Int,
        grades: ReviewGrades,
        review: String
    ): Result<Boolean>

}
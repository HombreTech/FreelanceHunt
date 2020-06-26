package tech.hombre.domain.repository

import tech.hombre.domain.model.Result

interface RejectConditionsRepository {

    suspend fun rejectConditions(
        workspaceId: Int
    ): Result<Boolean>

}
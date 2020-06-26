package tech.hombre.domain.repository

import tech.hombre.domain.model.Result

interface AcceptConditionsRepository {

    suspend fun acceptConditions(
        workspaceId: Int
    ): Result<Boolean>

}
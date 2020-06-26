package tech.hombre.domain.repository

import tech.hombre.domain.model.*

interface ExtendConditionsRepository {

    suspend fun extendConditions(
        projectId: Int,
        days: Int
    ): Result<Workspace>

}
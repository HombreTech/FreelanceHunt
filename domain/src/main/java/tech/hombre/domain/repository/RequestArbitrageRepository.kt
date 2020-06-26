package tech.hombre.domain.repository

import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.Result

interface RequestArbitrageRepository {

    suspend fun requestArbitrage(
        workspaceId: Int,
        comment: String
    ): Result<Boolean>

}
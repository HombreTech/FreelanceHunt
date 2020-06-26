package tech.hombre.domain.repository

import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.Result

interface WorkspaceCloseRepository {

    suspend fun closeWorkspace(
        workspaceId: Int,
        review: String
    ): Result<Boolean>

}
package tech.hombre.domain.repository

import tech.hombre.domain.model.Result

interface ProjectChooseBidRepository {

    suspend fun chooseBid(projectId: Int, bidId: Int, comment: String): Result<Boolean>

}
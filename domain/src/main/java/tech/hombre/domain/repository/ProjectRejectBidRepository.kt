package tech.hombre.domain.repository

import tech.hombre.domain.model.Result

interface ProjectRejectBidRepository {

    suspend fun rejectBid(projectId: Int, bidId: Int): Result<Boolean>

}
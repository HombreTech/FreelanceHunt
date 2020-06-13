package tech.hombre.domain.repository

import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.ProjectBid
import tech.hombre.domain.model.Result

interface ProjectRevokeBidRepository {

    suspend fun revokeBid(projectId: Int, bidId: Int): Result<Boolean>

}
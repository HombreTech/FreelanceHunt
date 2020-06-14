package tech.hombre.data.repository.bids

import tech.hombre.data.networking.BidsApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.query
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.ProjectRevokeBidRepository

class ProjectRevokeBidRepositoryImpl(
    private val bidsApi: BidsApi
) : BaseRepository<Boolean, DomainMapper<Boolean>>(),
    ProjectRevokeBidRepository {

    override suspend fun revokeBid(projectId: Int, bidId: Int): Result<Boolean> {
        return bidsApi.revokeProjectBid(projectId, bidId).query()
    }
}

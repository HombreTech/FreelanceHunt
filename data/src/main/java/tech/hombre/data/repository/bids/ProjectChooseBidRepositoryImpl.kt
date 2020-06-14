package tech.hombre.data.repository.bids

import tech.hombre.data.networking.BidsApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.query
import tech.hombre.data.networking.model.ChooseBidBody
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.ProjectChooseBidRepository

class ProjectChooseBidRepositoryImpl(
    private val bidsApi: BidsApi
) : BaseRepository<Boolean, DomainMapper<Boolean>>(),
    ProjectChooseBidRepository {

    override suspend fun chooseBid(projectId: Int, bidId: Int, comment: String): Result<Boolean> {
        return bidsApi.chooseWinnerProjectBid(projectId, bidId, ChooseBidBody(comment)).query()
    }
}

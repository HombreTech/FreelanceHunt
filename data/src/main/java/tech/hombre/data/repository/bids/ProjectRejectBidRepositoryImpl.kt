package tech.hombre.data.repository.bids

import tech.hombre.data.networking.BidsApi
import tech.hombre.data.networking.GENERAL_NETWORK_ERROR
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.Failure
import tech.hombre.domain.model.HttpError
import tech.hombre.domain.model.Result
import tech.hombre.domain.model.Success
import tech.hombre.domain.repository.ProjectRejectBidRepository
import tech.hombre.domain.repository.ProjectRevokeBidRepository

class ProjectRejectBidRepositoryImpl(
    private val bidsApi: BidsApi
) : BaseRepository<Boolean, DomainMapper<Boolean>>(),
    ProjectRejectBidRepository {

    override suspend fun rejectBid(projectId: Int, bidId: Int): Result<Boolean> {
        return if (bidsApi.rejectProjectBid(projectId, bidId).isSuccessful
        ) Success(true) else Failure(
            HttpError(Throwable(GENERAL_NETWORK_ERROR))
        )
    }
}

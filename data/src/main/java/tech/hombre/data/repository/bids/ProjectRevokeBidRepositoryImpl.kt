package tech.hombre.data.repository.bids

import tech.hombre.data.networking.BidsApi
import tech.hombre.data.networking.NOT_HAVE_INTERNET_CONNECTION
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.query
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.Failure
import tech.hombre.domain.model.HttpError
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.ProjectRevokeBidRepository

class ProjectRevokeBidRepositoryImpl(
    private val bidsApi: BidsApi
) : BaseRepository<Boolean, DomainMapper<Boolean>>(),
    ProjectRevokeBidRepository {

    override suspend fun revokeBid(projectId: Int, bidId: Int): Result<Boolean> =
        if (hasNetworkAccess()) bidsApi.revokeProjectBid(projectId, bidId).query() else Failure(
            HttpError(Throwable(NOT_HAVE_INTERNET_CONNECTION))
        )
}

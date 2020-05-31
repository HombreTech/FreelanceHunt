package tech.hombre.data.repository.bids

import tech.hombre.data.networking.BidsApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.getData
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.BidsListRepository

class BidsRepositoryImpl(
    private val bidsApi: BidsApi
) : BaseRepository<MyBidsList, DomainMapper<MyBidsList>>(),
    BidsListRepository {
    override suspend fun getMyBidsList(url: String): Result<MyBidsList> {
        return fetchData(
            dataProvider = { bidsApi.getMyBids(url).getData() }
        )
    }
}
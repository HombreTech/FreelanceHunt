package tech.hombre.data.repository.contestslist

import tech.hombre.data.networking.ContestsApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.getData
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.ContestDetail
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.ContestDetailRepository

class ContestDetailRepositoryImpl(
    private val contestApi: ContestsApi
) : BaseRepository<ContestDetail, DomainMapper<ContestDetail>>(),
    ContestDetailRepository {

    override suspend fun getContestDetail(contestId: Int): Result<ContestDetail> {
        return fetchData(
            dataProvider = { contestApi.getContestDetail(contestId).getData() }
        )
    }
}
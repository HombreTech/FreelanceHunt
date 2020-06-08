package tech.hombre.data.repository.mycontests

import tech.hombre.data.networking.ContestsApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.getData
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.MyContestsList
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.MyContestsListRepository

class MyContestsListRepositoryImpl(
    private val contestsApi: ContestsApi
) : BaseRepository<MyContestsList, DomainMapper<MyContestsList>>(),
    MyContestsListRepository {
    override suspend fun getMyContestsList(url: String): Result<MyContestsList> {
        return fetchData(
            dataProvider = { contestsApi.getMyContestsList(url).getData() }
        )
    }
}
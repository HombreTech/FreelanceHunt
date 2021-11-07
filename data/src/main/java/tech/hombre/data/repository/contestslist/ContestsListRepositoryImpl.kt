package tech.hombre.data.repository.contestslist

import tech.hombre.data.BuildConfig
import tech.hombre.data.database.dao.ContestsListDao
import tech.hombre.data.database.model.ContestsListEntity
import tech.hombre.data.networking.ContestsApi
import tech.hombre.data.networking.base.getData
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.ContestsList
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.ContestsListRepository

class ContestsListRepositoryImpl(
    private val contestsApi: ContestsApi,
    private val contestsListDao: ContestsListDao
) : BaseRepository<ContestsList, ContestsListEntity>(),
    ContestsListRepository {
    override suspend fun getContestsList(page: Int, skills: String?): Result<ContestsList> {
        val url = BuildConfig.BASE_URL + "contests?page[number]=$page&filter[skill_id]=$skills"
        return fetchData(
            apiDataProvider = {
                contestsApi.getContestsList(page, skills).getData(
                    fetchFromCacheAction = { contestsListDao.getContestsList(url) },
                    cacheAction = { contestsListDao.saveContestsList(it) })
            },
            dbDataProvider = { contestsListDao.getContestsList(url) }
        )
    }
}
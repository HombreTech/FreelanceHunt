package tech.hombre.data.repository.cities

import tech.hombre.data.database.dao.CitiesDao
import tech.hombre.data.database.model.CitiesEntity
import tech.hombre.data.networking.CitiesApi
import tech.hombre.data.networking.base.getData
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.Cities
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.CitiesRepository

class CitiesRepositoryImpl(
    private val citiesApi: CitiesApi,
    private val citiesDao: CitiesDao
) : BaseRepository<Cities, CitiesEntity>(),
    CitiesRepository {
    override suspend fun getCities(countryId: Int): Result<Cities> {
        return fetchData(
            apiDataProvider = {
                citiesApi.getCities(countryId).getData(
                    fetchFromCacheAction = { citiesDao.getCities(countryId) },
                    cacheAction = { citiesDao.saveCities(it.apply { it.countryId = countryId }) })
            },
            dbDataProvider = { citiesDao.getCities(countryId) },
            fromCache = true
        )
    }
}
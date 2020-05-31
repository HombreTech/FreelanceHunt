package tech.hombre.data.repository.countries

import tech.hombre.data.database.dao.CountriesDao
import tech.hombre.data.database.model.CountriesEntity
import tech.hombre.data.networking.CountriesApi
import tech.hombre.data.networking.base.getData
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.Countries
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.CountriesRepository

class CountriesRepositoryImpl(
    private val countriesApi: CountriesApi,
    private val countriesDao: CountriesDao
) : BaseRepository<Countries, CountriesEntity>(),
    CountriesRepository {
    override suspend fun getCountries(): Result<Countries> {
        return fetchData(
            apiDataProvider = {
                countriesApi.getCountries().getData(
                    fetchFromCacheAction = { countriesDao.getCountries() },
                    cacheAction = { countriesDao.saveCountries(it) })
            },
            dbDataProvider = { countriesDao.getCountries() },
            fromCache = true
        )
    }
}
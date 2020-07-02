package tech.hombre.data.repository.employerslist

import tech.hombre.data.networking.EmployersApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.getData
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.EmployersList
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.EmployersListRepository

class EmployersListRepositoryImpl(
    private val employersApi: EmployersApi
) : BaseRepository<EmployersList, DomainMapper<EmployersList>>(),
    EmployersListRepository {
    override suspend fun getEmployersList(page: String, countryId: Int, cityId: Int): Result<EmployersList> {
        return fetchData(
            dataProvider = { employersApi.getEmployersList(page, countryId, cityId).getData() }
        )
    }
}
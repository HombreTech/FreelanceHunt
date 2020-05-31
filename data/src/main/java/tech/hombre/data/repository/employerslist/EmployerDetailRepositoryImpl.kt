package tech.hombre.data.repository.employerslist

import tech.hombre.data.networking.EmployersApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.getData
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.EmployerDetail
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.EmployerDetailRepository

class EmployerDetailRepositoryImpl(
    private val employersApi: EmployersApi
) : BaseRepository<EmployerDetail, DomainMapper<EmployerDetail>>(),
    EmployerDetailRepository {

    override suspend fun getEmployerDetail(profileId: Int): Result<EmployerDetail> {
        return fetchData(
            dataProvider = { employersApi.getEmployerDetail(profileId).getData() }
        )
    }
}
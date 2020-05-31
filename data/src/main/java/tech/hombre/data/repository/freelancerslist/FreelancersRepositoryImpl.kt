package tech.hombre.data.repository.freelancerslist

import tech.hombre.data.networking.FreelancersApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.getData
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.FreelancersList
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.FreelancersRepository

class FreelancersRepositoryImpl(
    private val freelancersApi: FreelancersApi
) : BaseRepository<FreelancersList, DomainMapper<FreelancersList>>(),
    FreelancersRepository {
    override suspend fun getFreelancersList(url: String): Result<FreelancersList> {
        return fetchData(
            dataProvider = { freelancersApi.getFreelancersList(url).getData() }
        )
    }
}
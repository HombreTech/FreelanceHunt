package tech.hombre.data.repository.freelancerslist

import tech.hombre.data.networking.FreelancersApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.getData
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.FreelancerDetail
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.FreelancerDetailRepository

class FreelancerDetailRepositoryImpl(
    private val freelancersApi: FreelancersApi
) : BaseRepository<FreelancerDetail, DomainMapper<FreelancerDetail>>(),
    FreelancerDetailRepository {

    override suspend fun getFreelancerDetail(profileId: Int): Result<FreelancerDetail> {
        return fetchData(
            dataProvider = { freelancersApi.getFreelancerDetail(profileId).getData() }
        )
    }
}
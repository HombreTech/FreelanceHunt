package tech.hombre.data.repository.freelancerslist

import tech.hombre.data.networking.FreelancersApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.getData
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.FreelancerReviews
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.FreelancerReviewsRepository

class FreelancerReviewsRepositoryImpl(
    private val freelancersApi: FreelancersApi
) : BaseRepository<FreelancerReviews, DomainMapper<FreelancerReviews>>(),
    FreelancerReviewsRepository {

    override suspend fun getFreelancerReviews(url: String): Result<FreelancerReviews> {
        return fetchData(
            dataProvider = { freelancersApi.getFreelancerReviews(url).getData() }
        )
    }
}
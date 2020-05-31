package tech.hombre.data.repository.employerslist

import tech.hombre.data.networking.EmployersApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.getData
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.EmployerReviews
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.EmployerReviewsRepository

class EmployerReviewsRepositoryImpl(
    private val employersApi: EmployersApi
) : BaseRepository<EmployerReviews, DomainMapper<EmployerReviews>>(),
    EmployerReviewsRepository {

    override suspend fun getEmployerReviews(url: String): Result<EmployerReviews> {
        return fetchData(
            dataProvider = { employersApi.getEmployerReviews(url).getData() }
        )
    }
}
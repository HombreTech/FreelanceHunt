package tech.hombre.domain.repository

import tech.hombre.domain.model.EmployerReviews
import tech.hombre.domain.model.Result

interface EmployerReviewsRepository {
    suspend fun getEmployerReviews(url: String): Result<EmployerReviews>

}
package tech.hombre.domain.repository

import tech.hombre.domain.model.FreelancerReviews
import tech.hombre.domain.model.Result

interface FreelancerReviewsRepository {
    suspend fun getFreelancerReviews(url: String): Result<FreelancerReviews>

}
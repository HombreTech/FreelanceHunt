package tech.hombre.domain.interaction.freelancerslist.reviews

import tech.hombre.domain.base.BaseUseCase
import tech.hombre.domain.model.FreelancerReviews
import tech.hombre.domain.model.Result

interface GetFreelancerReviewsUseCase : BaseUseCase<String, FreelancerReviews> {

    override suspend operator fun invoke(url: String): Result<FreelancerReviews>
}
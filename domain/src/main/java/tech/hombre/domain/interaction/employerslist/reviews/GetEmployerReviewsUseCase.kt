package tech.hombre.domain.interaction.employerslist.reviews

import tech.hombre.domain.base.BaseUseCase
import tech.hombre.domain.model.EmployerReviews
import tech.hombre.domain.model.Result

interface GetEmployerReviewsUseCase : BaseUseCase<String, EmployerReviews> {

    override suspend operator fun invoke(url: String): Result<EmployerReviews>
}
package tech.hombre.data.networking.model

import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.EmployerReviews

data class EmployerReviewsResponse(
    val data: List<EmployerReviews.Data>,
    val links: EmployerReviews.Links
) : DomainMapper<EmployerReviews> {

    override fun mapToDomainModel() = EmployerReviews(data, links)
}


package tech.hombre.data.networking.model

import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.FreelancerReviews

data class FreelancerReviewsResponse(
    val data: List<FreelancerReviews.Data>,
    val links: FreelancerReviews.Links
) : DomainMapper<FreelancerReviews> {

    override fun mapToDomainModel() = FreelancerReviews(data, links)
}


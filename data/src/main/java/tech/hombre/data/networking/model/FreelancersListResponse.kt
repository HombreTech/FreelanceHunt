package tech.hombre.data.networking.model

import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.FreelancerDetail
import tech.hombre.domain.model.FreelancersList

data class FreelancersListResponse(
    val data: List<FreelancerDetail.Data>,
    val links: FreelancersList.Links
) : DomainMapper<FreelancersList> {

    override fun mapToDomainModel() = FreelancersList(data, links)
}


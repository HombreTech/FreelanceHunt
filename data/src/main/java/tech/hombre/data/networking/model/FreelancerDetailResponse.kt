package tech.hombre.data.networking.model

import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.FreelancerDetail

data class FreelancerDetailResponse(
    val data: FreelancerDetail.Data,
    val links: FreelancerDetail.Links
) : DomainMapper<FreelancerDetail> {

    override fun mapToDomainModel() = FreelancerDetail(data, links)
}


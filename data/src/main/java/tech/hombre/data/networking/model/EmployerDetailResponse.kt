package tech.hombre.data.networking.model

import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.EmployerDetail

data class EmployerDetailResponse(
    val data: EmployerDetail.Data,
    val links: EmployerDetail.Links
) : DomainMapper<EmployerDetail> {

    override fun mapToDomainModel() = EmployerDetail(data, links)
}


package tech.hombre.data.networking.model

import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.ContestDetail

data class ContestDetailResponse(
    val data: ContestDetail.Data,
    val links: ContestDetail.Links
) : DomainMapper<ContestDetail> {

    override fun mapToDomainModel() = ContestDetail(data, links)
}


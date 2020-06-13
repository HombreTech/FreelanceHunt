package tech.hombre.data.networking.model

import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.ProjectBid

data class ProjectAddBidResponse(
    val data: ProjectBid.Data
) : DomainMapper<ProjectBid.Data> {

    override fun mapToDomainModel() = data
}


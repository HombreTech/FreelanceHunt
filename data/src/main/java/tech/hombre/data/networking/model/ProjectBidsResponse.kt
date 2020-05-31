package tech.hombre.data.networking.model

import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.ProjectBid

data class ProjectBidsResponse(
    val data: List<ProjectBid.Data>,
    val links: ProjectBid.Links
) : DomainMapper<ProjectBid> {

    override fun mapToDomainModel() = ProjectBid(data, links)
}


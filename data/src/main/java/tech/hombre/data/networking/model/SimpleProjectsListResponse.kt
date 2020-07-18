package tech.hombre.data.networking.model

import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.ProjectDetail
import tech.hombre.domain.model.ProjectsList

data class SimpleProjectsListResponse(
    val data: List<ProjectDetail.Data>,
    val links: ProjectsList.Links
) : DomainMapper<ProjectsList> {

    override fun mapToDomainModel() = ProjectsList(data, links)
}


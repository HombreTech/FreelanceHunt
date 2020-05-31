package tech.hombre.data.networking.model

import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.ProjectDetail

data class ProjectDetailResponse(
    val data: ProjectDetail.Data,
    val links: ProjectDetail.Links
) : DomainMapper<ProjectDetail> {

    override fun mapToDomainModel() = ProjectDetail(data, links)
}


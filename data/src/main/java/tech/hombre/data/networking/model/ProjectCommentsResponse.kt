package tech.hombre.data.networking.model

import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.ProjectComment

data class ProjectCommentsResponse(
    val data: List<ProjectComment.Data>,
    val links: ProjectComment.Links
) : DomainMapper<ProjectComment> {

    override fun mapToDomainModel() = ProjectComment(data, links)
}


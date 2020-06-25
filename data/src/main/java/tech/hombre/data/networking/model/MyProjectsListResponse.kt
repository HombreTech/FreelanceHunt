package tech.hombre.data.networking.model

import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.MyProjectsList
import tech.hombre.domain.model.ProjectDetail

data class MyProjectsListResponse(
    val data: List<ProjectDetail.Data>,
    val links: MyProjectsList.Links
) : DomainMapper<MyProjectsList> {

    override fun mapToDomainModel() = MyProjectsList(data, links)
}

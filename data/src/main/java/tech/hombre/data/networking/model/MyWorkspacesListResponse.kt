package tech.hombre.data.networking.model

import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.WorkspacesList
import tech.hombre.domain.model.WorkspaceDetail

data class MyWorkspacesListResponse(
    val data: List<WorkspaceDetail.Data>,
    val links: WorkspacesList.Links
) : DomainMapper<WorkspacesList> {

    override fun mapToDomainModel() = WorkspacesList(data, links)
}

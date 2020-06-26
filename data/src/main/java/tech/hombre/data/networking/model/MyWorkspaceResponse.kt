package tech.hombre.data.networking.model

import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.Workspace
import tech.hombre.domain.model.WorkspaceDetail

data class MyWorkspaceResponse(
    val data: WorkspaceDetail.Data,
    val links: Workspace.Links
) : DomainMapper<Workspace> {

    override fun mapToDomainModel() = Workspace(data, links)
}

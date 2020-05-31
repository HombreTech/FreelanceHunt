package tech.hombre.data.networking.model

import tech.hombre.data.database.model.ProjectsListEntity
import tech.hombre.data.networking.base.RoomMapper
import tech.hombre.domain.model.ProjectsList

data class ProjectsListResponse(
    val data: List<ProjectsList.Data>,
    val links: ProjectsList.Links
) : RoomMapper<ProjectsListEntity> {

    override fun mapToRoomEntity() =
        ProjectsListEntity(url = links.self, projectsList = ProjectsList(data, links))
}


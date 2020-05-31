package tech.hombre.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import tech.hombre.data.database.PROJECTS_LIST_TABLE_NAME
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.ProjectsList

@Entity(tableName = PROJECTS_LIST_TABLE_NAME)
data class ProjectsListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val url: String,
    val projectsList: ProjectsList?
) : DomainMapper<ProjectsList> {

    override fun mapToDomainModel() = projectsList ?: ProjectsList()
}

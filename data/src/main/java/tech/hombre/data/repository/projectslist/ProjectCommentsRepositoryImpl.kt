package tech.hombre.data.repository.projectslist

import tech.hombre.data.networking.ProjectsApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.getData
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.ProjectComment
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.ProjectCommentsRepository

class ProjectCommentsRepositoryImpl(
    private val projectApi: ProjectsApi
) : BaseRepository<ProjectComment, DomainMapper<ProjectComment>>(),
    ProjectCommentsRepository {

    override suspend fun getProjectComments(projectId: Int): Result<ProjectComment> {
        return fetchData(
            dataProvider = { projectApi.getProjectComments(projectId).getData() }
        )
    }
}
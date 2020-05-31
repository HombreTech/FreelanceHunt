package tech.hombre.data.repository.projectslist

import tech.hombre.data.networking.ProjectsApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.getData
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.ProjectDetail
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.ProjectDetailRepository

class ProjectDetailRepositoryImpl(
    private val projectsApi: ProjectsApi
) : BaseRepository<ProjectDetail, DomainMapper<ProjectDetail>>(),
    ProjectDetailRepository {

    override suspend fun getProjectDetail(url: String): Result<ProjectDetail> {
        return fetchData(
            dataProvider = { projectsApi.getProjectDetail(url).getData() }
        )
    }
}
package tech.hombre.data.repository.projectslist

import tech.hombre.data.networking.ProjectsApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.getData
import tech.hombre.data.networking.model.ExtendProjectBody
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.ProjectDetail
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.ExtendProjectRepository

class ExtendProjectRepositoryImpl(
    private val projectsApi: ProjectsApi
) : BaseRepository<ProjectDetail, DomainMapper<ProjectDetail>>(),
    ExtendProjectRepository {

    override suspend fun extendProject(
        projectId: Int,
        expiredAt: String
    ): Result<ProjectDetail> {
        return fetchData(
            dataProvider = {
                projectsApi.extendProject(
                    projectId,
                    ExtendProjectBody(
                        expiredAt
                    )
                ).getData()
            }
        )
    }
}
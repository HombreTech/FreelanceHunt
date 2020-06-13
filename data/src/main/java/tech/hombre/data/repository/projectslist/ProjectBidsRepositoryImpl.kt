package tech.hombre.data.repository.projectslist

import tech.hombre.data.networking.ProjectsApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.getData
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.ProjectBid
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.ProjectBidsRepository

class ProjectBidsRepositoryImpl(
    private val projectApi: ProjectsApi
) : BaseRepository<ProjectBid, DomainMapper<ProjectBid>>(),
    ProjectBidsRepository {

    override suspend fun getProjectBids(projectId: Int): Result<ProjectBid> {
        return fetchData(
            dataProvider = { projectApi.getProjectBids(projectId).getData() }
        )
    }
}
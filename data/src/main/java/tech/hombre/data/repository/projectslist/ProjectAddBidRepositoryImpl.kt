package tech.hombre.data.repository.projectslist

import tech.hombre.data.networking.ProjectsApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.getData
import tech.hombre.data.networking.model.AddBidBody
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.ProjectBid
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.ProjectAddBidRepository

class ProjectAddBidRepositoryImpl(
    private val projectApi: ProjectsApi
) : BaseRepository<ProjectBid.Data, DomainMapper<ProjectBid.Data>>(),
    ProjectAddBidRepository {

    override suspend fun addProjectBids(
        projectId: Int,
        days: Int,
        budget: MyBidsList.Data.Attributes.Budget,
        safeType: String,
        comment: String,
        isHidden: Boolean
    ): Result<ProjectBid.Data> {
        return fetchData(
            dataProvider = { projectApi.addProjectBid(projectId, AddBidBody(days, budget, safeType, comment, isHidden)).getData() }
        )
    }
}
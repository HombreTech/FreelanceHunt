package tech.hombre.data.repository.projectslist

import tech.hombre.data.networking.ProjectsApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.getData
import tech.hombre.data.networking.model.AmendProjectBody
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.ProjectDetail
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.AmendProjectRepository

class AmendProjectRepositoryImpl(
    private val projectsApi: ProjectsApi
) : BaseRepository<ProjectDetail, DomainMapper<ProjectDetail>>(),
    AmendProjectRepository {

    override suspend fun amendProject(
        projectId: Int,
        budget: MyBidsList.Data.Attributes.Budget,
        updateHtml: String,
        skills: ArrayList<Int>
    ): Result<ProjectDetail> {
        return fetchData(
            dataProvider = {
                projectsApi.amendProject(
                    projectId,
                    AmendProjectBody(
                        budget,
                        updateHtml,
                        skills
                    )
                ).getData()
            }
        )
    }
}
package tech.hombre.data.repository.projectslist

import tech.hombre.data.networking.ProjectsApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.getData
import tech.hombre.data.networking.model.UpdateProjectBody
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.ProjectDetail
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.UpdateProjectRepository

class UpdateProjectRepositoryImpl(
    private val projectsApi: ProjectsApi
) : BaseRepository<ProjectDetail, DomainMapper<ProjectDetail>>(),
    UpdateProjectRepository {

    override suspend fun updateProject(
        projectId: Int,
        name: String,
        budget: MyBidsList.Data.Attributes.Budget,
        safeType: String,
        descriptionHtml: String,
        skills: ArrayList<Int>,
        expired_at: String
    ): Result<ProjectDetail> {
        return fetchData(
            dataProvider = {
                projectsApi.updateProject(
                    projectId,
                    UpdateProjectBody(
                        name,
                        budget,
                        safeType,
                        descriptionHtml,
                        skills,
                        expired_at
                    )
                ).getData()
            }
        )
    }
}
package tech.hombre.data.repository.projectslist

import tech.hombre.data.networking.ProjectsApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.getData
import tech.hombre.data.networking.model.NewPersonalProjectBody
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.ProjectDetail
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.NewPersonalProjectRepository

class NewPersonalProjectRepositoryImpl(
    private val projectsApi: ProjectsApi
) : BaseRepository<ProjectDetail, DomainMapper<ProjectDetail>>(),
    NewPersonalProjectRepository {

    override suspend fun newProject(
        name: String,
        freelancerId: Int,
        is_personal: Boolean,
        budget: MyBidsList.Data.Attributes.Budget,
        safeType: String,
        descriptionHtml: String,
        skills: ArrayList<Int>,
        expired_at: String
    ): Result<ProjectDetail> {
        return fetchData(
            dataProvider = {
                projectsApi.newPersonalProject(
                    NewPersonalProjectBody(
                        name,
                        freelancerId,
                        is_personal,
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
package tech.hombre.domain.interaction.projectslist

import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.ProjectDetail
import tech.hombre.domain.model.Result

interface UpdateProjectUseCase {

    suspend operator fun invoke(projectId: Int,
                                name: String,
                                budget: MyBidsList.Data.Attributes.Budget,
                                safeType: String,
                                descriptionHtml: String,
                                skills: ArrayList<Int>,
                                expiredAt: String): Result<ProjectDetail>
}
package tech.hombre.domain.interaction.projectslist

import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.ProjectDetail
import tech.hombre.domain.model.Result

interface AmendProjectUseCase {

    suspend operator fun invoke(projectId: Int,
                                budget: MyBidsList.Data.Attributes.Budget,
                                updateHtml: String,
                                skills: ArrayList<Int>): Result<ProjectDetail>
}
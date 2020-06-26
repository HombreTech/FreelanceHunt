package tech.hombre.domain.interaction.projectslist

import tech.hombre.domain.base.BaseUseCase
import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.ProjectDetail
import tech.hombre.domain.model.ProjectsList
import tech.hombre.domain.model.Result

interface NewProjectUseCase {

    suspend operator fun invoke(name: String,
                                budget: MyBidsList.Data.Attributes.Budget,
                                safeType: String,
                                descriptionHtml: String,
                                skills: ArrayList<Int>,
                                expired_at: String,
                                isForPlus: Boolean): Result<ProjectDetail>
}
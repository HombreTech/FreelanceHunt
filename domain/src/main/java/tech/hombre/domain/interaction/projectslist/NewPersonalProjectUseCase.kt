package tech.hombre.domain.interaction.projectslist

import tech.hombre.domain.base.BaseUseCase
import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.ProjectDetail
import tech.hombre.domain.model.ProjectsList
import tech.hombre.domain.model.Result

interface NewPersonalProjectUseCase  {

    suspend operator fun invoke(name: String,
                                freelancerId: Int,
                                is_personal: Boolean,
                                budget: MyBidsList.Data.Attributes.Budget,
                                safeType: String,
                                descriptionHtml: String,
                                skills: ArrayList<Int>,
                                expired_at: String): Result<ProjectDetail>
}
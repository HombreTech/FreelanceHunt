package tech.hombre.domain.interaction.projectslist.bids

import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.ProjectBid
import tech.hombre.domain.model.Result

interface AddProjectBidsUseCase {

    suspend operator fun invoke(
        projectId: Int, days: Int,
        budget: MyBidsList.Data.Attributes.Budget,
        safeType: String,
        comment: String,
        isHidden: Boolean
    ): Result<ProjectBid.Data>
}
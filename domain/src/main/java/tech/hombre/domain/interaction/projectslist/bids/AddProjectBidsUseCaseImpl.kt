package tech.hombre.domain.interaction.projectslist.bids

import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.ProjectBid
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.ProjectAddBidRepository

class AddProjectBidsUseCaseImpl(private val projectBidsRepository: ProjectAddBidRepository) :
    AddProjectBidsUseCase {

    override suspend operator fun invoke(
        projectId: Int,
        days: Int,
        budget: MyBidsList.Data.Attributes.Budget,
        safeType: String,
        comment: String,
        isHidden: Boolean
    ): Result<ProjectBid.Data> =
        projectBidsRepository.addProjectBids(projectId, days, budget, safeType, comment, isHidden)
}
package tech.hombre.domain.interaction.projectslist.bids

import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.ProjectBid
import tech.hombre.domain.model.Result

interface RevokeProjectBidsUseCase {

    suspend operator fun invoke(projectId: Int, bidId: Int): Result<Boolean>
}
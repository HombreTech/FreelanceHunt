package tech.hombre.domain.interaction.projectslist.bids

import tech.hombre.domain.model.Result

interface RejectProjectBidsUseCase {

    suspend operator fun invoke(projectId: Int, bidId: Int): Result<Boolean>
}
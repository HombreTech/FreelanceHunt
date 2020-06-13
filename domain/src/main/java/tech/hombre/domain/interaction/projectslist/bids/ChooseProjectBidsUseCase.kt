package tech.hombre.domain.interaction.projectslist.bids

import tech.hombre.domain.model.Result

interface ChooseProjectBidsUseCase {

    suspend operator fun invoke(projectId: Int, bidId: Int, comment: String): Result<Boolean>
}
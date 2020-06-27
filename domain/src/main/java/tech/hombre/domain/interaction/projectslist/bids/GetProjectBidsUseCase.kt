package tech.hombre.domain.interaction.projectslist.bids

import tech.hombre.domain.model.ProjectBid
import tech.hombre.domain.model.Result

interface GetProjectBidsUseCase {

    suspend operator fun invoke(projectId: Int, status: String): Result<ProjectBid>
}
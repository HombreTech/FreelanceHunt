package tech.hombre.domain.interaction.projectslist.bids

import tech.hombre.domain.base.BaseUseCase
import tech.hombre.domain.model.ProjectBid
import tech.hombre.domain.model.Result

interface GetProjectBidsUseCase : BaseUseCase<Int, ProjectBid> {

    override suspend operator fun invoke(projectId: Int): Result<ProjectBid>
}
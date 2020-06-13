package tech.hombre.domain.interaction.projectslist.bids

import tech.hombre.domain.repository.ProjectRejectBidRepository


class RejectProjectBidsUseCaseImpl(private val projectBidsRepository: ProjectRejectBidRepository) :
    RejectProjectBidsUseCase {

    override suspend operator fun invoke(projectId: Int, bidId: Int) = projectBidsRepository.rejectBid(projectId, bidId)
}
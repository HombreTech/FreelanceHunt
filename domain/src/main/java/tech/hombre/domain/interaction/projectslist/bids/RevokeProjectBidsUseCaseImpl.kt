package tech.hombre.domain.interaction.projectslist.bids

import tech.hombre.domain.repository.ProjectRevokeBidRepository

class RevokeProjectBidsUseCaseImpl(private val projectBidsRepository: ProjectRevokeBidRepository) :
    RevokeProjectBidsUseCase {

    override suspend operator fun invoke(projectId: Int, bidId: Int) = projectBidsRepository.revokeBid(projectId, bidId)
}
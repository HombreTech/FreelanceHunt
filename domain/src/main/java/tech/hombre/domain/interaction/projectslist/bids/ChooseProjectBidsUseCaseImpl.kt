package tech.hombre.domain.interaction.projectslist.bids

import tech.hombre.domain.repository.ProjectChooseBidRepository

class ChooseProjectBidsUseCaseImpl(private val projectBidsRepository: ProjectChooseBidRepository) :
    ChooseProjectBidsUseCase {

    override suspend operator fun invoke(projectId: Int, bidId: Int, comment: String) = projectBidsRepository.chooseBid(projectId, bidId, comment)
}
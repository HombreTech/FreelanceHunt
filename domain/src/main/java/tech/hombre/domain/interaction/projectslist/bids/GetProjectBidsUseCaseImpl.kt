package tech.hombre.domain.interaction.projectslist.bids

import tech.hombre.domain.repository.ProjectBidsRepository

class GetProjectBidsUseCaseImpl(private val projectBidsRepository: ProjectBidsRepository) :
    GetProjectBidsUseCase {

    override suspend operator fun invoke(projectId: Int) =
        projectBidsRepository.getProjectBids(projectId)
}
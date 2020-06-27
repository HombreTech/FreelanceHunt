package tech.hombre.domain.interaction.projectslist

import tech.hombre.domain.repository.CloseProjectRepository

class CloseProjectUseCaseImpl(private val ProjectsListRepository: CloseProjectRepository) :
    CloseProjectUseCase {

    override suspend operator fun invoke(
        projectId: Int
    ) = ProjectsListRepository.closeProject(
        projectId
    )
}
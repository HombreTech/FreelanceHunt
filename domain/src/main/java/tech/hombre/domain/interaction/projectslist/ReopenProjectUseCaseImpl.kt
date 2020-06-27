package tech.hombre.domain.interaction.projectslist

import tech.hombre.domain.repository.ReopenProjectRepository

class ReopenProjectUseCaseImpl(private val ProjectsListRepository: ReopenProjectRepository) :
    ReopenProjectUseCase {

    override suspend operator fun invoke(
        projectId: Int,
        expiredAt: String
    ) = ProjectsListRepository.reopenProject(
        projectId,
        expiredAt
    )
}
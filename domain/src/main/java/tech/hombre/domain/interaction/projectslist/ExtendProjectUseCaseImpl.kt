package tech.hombre.domain.interaction.projectslist

import tech.hombre.domain.repository.ExtendProjectRepository

class ExtendProjectUseCaseImpl(private val ProjectsListRepository: ExtendProjectRepository) :
    ExtendProjectUseCase {

    override suspend operator fun invoke(
        projectId: Int,
        expiredAt: String
    ) = ProjectsListRepository.extendProject(
        projectId,
        expiredAt
    )
}
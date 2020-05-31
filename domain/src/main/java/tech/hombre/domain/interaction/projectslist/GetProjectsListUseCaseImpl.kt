package tech.hombre.domain.interaction.projectslist

import tech.hombre.domain.repository.ProjectsListRepository

class GetProjectsListUseCaseImpl(private val ProjectsListRepository: ProjectsListRepository) :
    GetProjectsListUseCase {

    override suspend operator fun invoke(url: String) = ProjectsListRepository.getProjectsList(url)
}
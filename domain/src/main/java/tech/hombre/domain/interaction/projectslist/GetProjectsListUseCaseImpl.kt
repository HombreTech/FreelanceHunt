package tech.hombre.domain.interaction.projectslist

import tech.hombre.domain.repository.ProjectsListRepository

class GetProjectsListUseCaseImpl(private val ProjectsListRepository: ProjectsListRepository) :
    GetProjectsListUseCase {

    override suspend operator fun invoke(page: Int, onlyMySkills: Boolean, onlyForPlus: Boolean, skills: String, employerId: Int?) = ProjectsListRepository.getProjectsList(page, onlyMySkills, onlyForPlus, skills, employerId)
}
package tech.hombre.domain.interaction.projectslist.detail

import tech.hombre.domain.repository.ProjectDetailRepository

class GetProjectDetailUseCaseImpl(private val projectDetailRepository: ProjectDetailRepository) :
    GetProjectDetailUseCase {

    override suspend operator fun invoke(url: String) =
        projectDetailRepository.getProjectDetail(url)
}
package tech.hombre.domain.interaction.myprojects

import tech.hombre.domain.repository.MyProjectsListRepository

class GetMyProjectsListUseCaseImpl(private val projectslistRepository: MyProjectsListRepository) :
    GetMyProjectsListUseCase {

    override suspend operator fun invoke(url: String) = projectslistRepository.getMyProjectsList(url)
}
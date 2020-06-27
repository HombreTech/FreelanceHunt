package tech.hombre.domain.interaction.projectslist

import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.repository.AmendProjectRepository

class AmendProjectUseCaseImpl(private val ProjectsListRepository: AmendProjectRepository) :
    AmendProjectUseCase {

    override suspend operator fun invoke(
        projectId: Int,
        budget: MyBidsList.Data.Attributes.Budget,
        updateHtml: String,
        skills: ArrayList<Int>
    ) = ProjectsListRepository.amendProject(
        projectId,
        budget,
        updateHtml,
        skills
    )
}
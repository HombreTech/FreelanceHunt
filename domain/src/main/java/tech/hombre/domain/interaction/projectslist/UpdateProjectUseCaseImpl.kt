package tech.hombre.domain.interaction.projectslist

import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.repository.UpdateProjectRepository

class UpdateProjectUseCaseImpl(private val ProjectsListRepository: UpdateProjectRepository) :
    UpdateProjectUseCase {

    override suspend operator fun invoke(
        projectId: Int,
        name: String,
        budget: MyBidsList.Data.Attributes.Budget,
        safeType: String,
        descriptionHtml: String,
        skills: ArrayList<Int>,
        expiredAt: String
    ) = ProjectsListRepository.updateProject(
        projectId,
        name,
        budget,
        safeType,
        descriptionHtml,
        skills,
        expiredAt
    )
}
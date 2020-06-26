package tech.hombre.domain.interaction.projectslist

import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.repository.NewProjectRepository

class NewProjectUseCaseImpl(private val ProjectsListRepository: NewProjectRepository) :
    NewProjectUseCase {

    override suspend operator fun invoke(
        name: String,
        budget: MyBidsList.Data.Attributes.Budget,
        safeType: String,
        descriptionHtml: String,
        skills: ArrayList<Int>,
        expired_at: String,
        isForPlus: Boolean
    ) = ProjectsListRepository.newProject(
        name,
        budget,
        safeType,
        descriptionHtml,
        skills,
        expired_at,
        isForPlus
    )
}
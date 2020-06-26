package tech.hombre.domain.interaction.projectslist

import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.repository.NewPersonalProjectRepository

class NewPersonalProjectUseCaseImpl(private val ProjectsListRepository: NewPersonalProjectRepository) :
    NewPersonalProjectUseCase {

    override suspend operator fun invoke(
        name: String,
        freelancerId: Int,
        is_personal: Boolean,
        budget: MyBidsList.Data.Attributes.Budget,
        safeType: String,
        descriptionHtml: String,
        skills: ArrayList<Int>,
        expired_at: String
    ) = ProjectsListRepository.newProject(
        name,
        freelancerId,
        is_personal,
        budget,
        safeType,
        descriptionHtml,
        skills,
        expired_at
    )
}
package tech.hombre.domain.interaction.myworkspaces

import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.repository.ProposeConditionsRepository

class ProposeConditionsUseCaseImpl(private val workspaceslistRepository: ProposeConditionsRepository) :
    ProposeConditionsUseCase {

    override suspend operator fun invoke(
        workspaceId: Int, days: Int,
        budget: MyBidsList.Data.Attributes.Budget,
        safeType: String,
        comment: String
    ) = workspaceslistRepository.proposeConditions(workspaceId, days, budget, safeType, comment)
}
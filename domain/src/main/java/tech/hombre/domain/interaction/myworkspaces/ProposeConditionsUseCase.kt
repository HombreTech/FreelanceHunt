package tech.hombre.domain.interaction.myworkspaces

import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.Result

interface ProposeConditionsUseCase {

    suspend operator fun invoke(
        workspaceId: Int,
        days: Int,
        budget: MyBidsList.Data.Attributes.Budget,
        safeType: String,
        comment: String
    ): Result<Boolean>
}
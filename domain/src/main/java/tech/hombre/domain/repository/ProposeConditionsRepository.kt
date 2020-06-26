package tech.hombre.domain.repository

import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.Result

interface ProposeConditionsRepository {

    suspend fun proposeConditions(
        workspaceId: Int, days: Int,
        budget: MyBidsList.Data.Attributes.Budget,
        safeType: String,
        comment: String
    ): Result<Boolean>

}
package tech.hombre.domain.repository

import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.ProjectBid
import tech.hombre.domain.model.Result

interface ProjectAddBidRepository {

    suspend fun addProjectBids(
        projectId: Int,
        days: Int,
        budget: MyBidsList.Data.Attributes.Budget,
        safeType: String,
        comment: String,
        isHidden: Boolean
    ): Result<ProjectBid.Data>

}
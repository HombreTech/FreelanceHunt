package tech.hombre.domain.repository

import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.ProjectDetail
import tech.hombre.domain.model.Result

interface AmendProjectRepository {
    suspend fun amendProject(
        projectId: Int,
        budget: MyBidsList.Data.Attributes.Budget,
        updateHtml: String,
        skills: ArrayList<Int>
    ): Result<ProjectDetail>
}
package tech.hombre.domain.repository

import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.ProjectDetail
import tech.hombre.domain.model.Result

interface UpdateProjectRepository {
    suspend fun updateProject(
        projectId: Int,
        name: String,
        budget: MyBidsList.Data.Attributes.Budget,
        safeType: String,
        descriptionHtml: String,
        skills: ArrayList<Int>,
        expired_at: String
    ): Result<ProjectDetail>
}
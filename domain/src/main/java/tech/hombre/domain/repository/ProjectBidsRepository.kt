package tech.hombre.domain.repository

import tech.hombre.domain.model.ProjectBid
import tech.hombre.domain.model.Result

interface ProjectBidsRepository {
    suspend fun getProjectBids(projectId: Int, status: String): Result<ProjectBid>

}
package tech.hombre.domain.repository

import tech.hombre.domain.model.ProjectDetail
import tech.hombre.domain.model.Result

interface ExtendProjectRepository {
    suspend fun extendProject(projectId: Int,
                           expiredAt: String): Result<ProjectDetail>
}
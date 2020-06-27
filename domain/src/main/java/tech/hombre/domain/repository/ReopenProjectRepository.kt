package tech.hombre.domain.repository

import tech.hombre.domain.model.Result

interface ReopenProjectRepository {
    suspend fun reopenProject(projectId: Int,
                           expiredAt: String): Result<Boolean>
}
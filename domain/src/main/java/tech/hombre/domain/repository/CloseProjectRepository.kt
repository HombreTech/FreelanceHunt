package tech.hombre.domain.repository

import tech.hombre.domain.model.Result

interface CloseProjectRepository {

    suspend fun closeProject(projectId: Int): Result<Boolean>

}
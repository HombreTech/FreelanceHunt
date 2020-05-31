package tech.hombre.domain.repository

import tech.hombre.domain.model.ProjectComment
import tech.hombre.domain.model.Result

interface ProjectCommentsRepository {
    suspend fun getProjectComments(projectId: Int): Result<ProjectComment>
}
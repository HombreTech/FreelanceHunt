package tech.hombre.domain.repository

import tech.hombre.domain.model.ProjectDetail
import tech.hombre.domain.model.Result

interface ProjectDetailRepository {
    suspend fun getProjectDetail(url: String): Result<ProjectDetail>
}
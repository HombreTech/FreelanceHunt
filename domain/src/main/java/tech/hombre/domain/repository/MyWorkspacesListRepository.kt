package tech.hombre.domain.repository

import tech.hombre.domain.model.Result
import tech.hombre.domain.model.WorkspacesList

interface MyWorkspacesListRepository {
    suspend fun getMyWorkspacesList(url: String): Result<WorkspacesList>
}
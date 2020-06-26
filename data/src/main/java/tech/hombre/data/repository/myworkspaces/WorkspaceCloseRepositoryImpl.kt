package tech.hombre.data.repository.myworkspaces

import tech.hombre.data.networking.WorkspacesApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.query
import tech.hombre.data.networking.model.CloseBody
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.WorkspaceCloseRepository

class WorkspaceCloseRepositoryImpl(
    private val workspaceApi: WorkspacesApi
) : BaseRepository<Boolean, DomainMapper<Boolean>>(),
    WorkspaceCloseRepository {

    override suspend fun closeWorkspace(
        workspaceId: Int,
        review: String
    ): Result<Boolean> {
        return workspaceApi.close(workspaceId, CloseBody(review)).query()
    }
}
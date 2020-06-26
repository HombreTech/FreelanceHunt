package tech.hombre.data.repository.myworkspaces

import tech.hombre.data.networking.WorkspacesApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.query
import tech.hombre.data.networking.model.CompleteBody
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.CompleteGrades
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.WorkspaceCompleteRepository

class WorkspaceCompleteRepositoryImpl(
    private val workspaceApi: WorkspacesApi
) : BaseRepository<Boolean, DomainMapper<Boolean>>(),
    WorkspaceCompleteRepository {

    override suspend fun completeWorkspace(
        workspaceId: Int,
        grades: CompleteGrades,
        review: String
    ): Result<Boolean> {
        return workspaceApi.complete(workspaceId, CompleteBody(grades, review)).query()
    }
}
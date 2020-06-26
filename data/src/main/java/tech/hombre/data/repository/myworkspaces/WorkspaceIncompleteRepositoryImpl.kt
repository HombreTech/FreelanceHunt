package tech.hombre.data.repository.myworkspaces

import tech.hombre.data.networking.WorkspacesApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.query
import tech.hombre.data.networking.model.CompleteBody
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.CompleteGrades
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.WorkspaceCompleteRepository
import tech.hombre.domain.repository.WorkspaceIncompleteRepository

class WorkspaceIncompleteRepositoryImpl(
    private val workspaceApi: WorkspacesApi
) : BaseRepository<Boolean, DomainMapper<Boolean>>(),
    WorkspaceIncompleteRepository {

    override suspend fun incompleteWorkspace(
        workspaceId: Int,
        grades: CompleteGrades,
        review: String
    ): Result<Boolean> {
        return workspaceApi.incomplete(workspaceId, CompleteBody(grades, review)).query()
    }
}
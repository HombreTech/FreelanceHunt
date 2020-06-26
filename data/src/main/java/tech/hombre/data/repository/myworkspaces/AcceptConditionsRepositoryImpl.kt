package tech.hombre.data.repository.myworkspaces

import tech.hombre.data.networking.WorkspacesApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.query
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.AcceptConditionsRepository

class AcceptConditionsRepositoryImpl(
    private val workspaceApi: WorkspacesApi
) : BaseRepository<Boolean, DomainMapper<Boolean>>(),
    AcceptConditionsRepository {

    override suspend fun acceptConditions(
        workspaceId: Int
    ): Result<Boolean> {
        return workspaceApi.acceptConditions(workspaceId).query()
    }
}
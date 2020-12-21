package tech.hombre.data.repository.myworkspaces

import tech.hombre.data.networking.NOT_HAVE_INTERNET_CONNECTION
import tech.hombre.data.networking.WorkspacesApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.query
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.Failure
import tech.hombre.domain.model.HttpError
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.RejectConditionsRepository

class RejectConditionsRepositoryImpl(
    private val workspaceApi: WorkspacesApi
) : BaseRepository<Boolean, DomainMapper<Boolean>>(),
    RejectConditionsRepository {

    override suspend fun rejectConditions(
        workspaceId: Int
    ): Result<Boolean> =
        if (hasNetworkAccess()) workspaceApi.rejectConditions(workspaceId).query() else Failure(
            HttpError(Throwable(NOT_HAVE_INTERNET_CONNECTION))
        )
}
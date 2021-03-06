package tech.hombre.data.repository.myworkspaces

import tech.hombre.data.networking.NOT_HAVE_INTERNET_CONNECTION
import tech.hombre.data.networking.WorkspacesApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.query
import tech.hombre.data.networking.model.RequestArbitrageBody
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.Failure
import tech.hombre.domain.model.HttpError
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.RequestArbitrageRepository

class RequestArbitrageRepositoryImpl(
    private val workspaceApi: WorkspacesApi
) : BaseRepository<Boolean, DomainMapper<Boolean>>(),
    RequestArbitrageRepository {

    override suspend fun requestArbitrage(
        workspaceId: Int,
        comment: String
    ): Result<Boolean> =
        if (hasNetworkAccess()) workspaceApi.requestArbitrage(
            workspaceId,
            RequestArbitrageBody(comment)
        ).query() else Failure(
            HttpError(Throwable(NOT_HAVE_INTERNET_CONNECTION))
        )

}
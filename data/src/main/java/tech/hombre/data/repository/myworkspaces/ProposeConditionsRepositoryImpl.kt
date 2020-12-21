package tech.hombre.data.repository.myworkspaces

import tech.hombre.data.networking.NOT_HAVE_INTERNET_CONNECTION
import tech.hombre.data.networking.WorkspacesApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.query
import tech.hombre.data.networking.model.ProposeConditionsBody
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.Failure
import tech.hombre.domain.model.HttpError
import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.ProposeConditionsRepository

class ProposeConditionsRepositoryImpl(
    private val workspaceApi: WorkspacesApi
) : BaseRepository<Boolean, DomainMapper<Boolean>>(),
    ProposeConditionsRepository {

    override suspend fun proposeConditions(
        workspaceId: Int,
        days: Int,
        budget: MyBidsList.Data.Attributes.Budget,
        safeType: String,
        comment: String
    ): Result<Boolean> =
        if (hasNetworkAccess()) workspaceApi.proposeConditions(workspaceId, ProposeConditionsBody(days, budget, safeType, comment)).query() else Failure(
            HttpError(Throwable(NOT_HAVE_INTERNET_CONNECTION))
        )
}
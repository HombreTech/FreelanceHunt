package tech.hombre.data.repository.myworkspaces

import tech.hombre.data.networking.WorkspacesApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.getData
import tech.hombre.data.networking.model.ExtendBody
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.Result
import tech.hombre.domain.model.Workspace
import tech.hombre.domain.repository.ExtendConditionsRepository

class ExtendConditionsRepositoryImpl(
    private val workspaceApi: WorkspacesApi
) : BaseRepository<Workspace, DomainMapper<Workspace>>(),
    ExtendConditionsRepository {

    override suspend fun extendConditions(
        workspaceId: Int,
        days: Int
    ): Result<Workspace> {
        return fetchData(
            dataProvider = { workspaceApi.extend(workspaceId, ExtendBody(days)).getData() }
        )
    }
}
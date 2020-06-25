package tech.hombre.data.repository.myworkspaces

import tech.hombre.data.networking.WorkspacesApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.getData
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.Result
import tech.hombre.domain.model.WorkspacesList
import tech.hombre.domain.repository.MyWorkspacesListRepository

class MyWorkspacesListRepositoryImpl(
    private val workspacesApi: WorkspacesApi
) : BaseRepository<WorkspacesList, DomainMapper<WorkspacesList>>(),
    MyWorkspacesListRepository {
    override suspend fun getMyWorkspacesList(url: String): Result<WorkspacesList> {
        return fetchData(
            dataProvider = { workspacesApi.getMyWorkspacesList(url).getData() }
        )
    }
}
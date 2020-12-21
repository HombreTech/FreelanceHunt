package tech.hombre.data.repository.projectslist

import tech.hombre.data.networking.NOT_HAVE_INTERNET_CONNECTION
import tech.hombre.data.networking.ProjectsApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.query
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.Failure
import tech.hombre.domain.model.HttpError
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.CloseProjectRepository

class CloseProjectRepositoryImpl(
    private val projectsApi: ProjectsApi
) : BaseRepository<Boolean, DomainMapper<Boolean>>(),
    CloseProjectRepository {

    override suspend fun closeProject(projectId: Int): Result<Boolean> =
        if (hasNetworkAccess()) projectsApi.closeProject(projectId).query() else Failure(
            HttpError(Throwable(
                NOT_HAVE_INTERNET_CONNECTION
            ))
        )
}

package tech.hombre.data.repository.projectslist

import tech.hombre.data.networking.NOT_HAVE_INTERNET_CONNECTION
import tech.hombre.data.networking.ProjectsApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.query
import tech.hombre.data.networking.model.ExtendProjectBody
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.Failure
import tech.hombre.domain.model.HttpError
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.ReopenProjectRepository

class ReopenProjectRepositoryImpl(
    private val projectsApi: ProjectsApi
) : BaseRepository<Boolean, DomainMapper<Boolean>>(),
    ReopenProjectRepository {

    override suspend fun reopenProject(projectId: Int, expiredAt: String): Result<Boolean> =
        if (hasNetworkAccess()) projectsApi.reopenProject(projectId, ExtendProjectBody(expiredAt)).query() else Failure(
            HttpError(Throwable(NOT_HAVE_INTERNET_CONNECTION))
        )
}
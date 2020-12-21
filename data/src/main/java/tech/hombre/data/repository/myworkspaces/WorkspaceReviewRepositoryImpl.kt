package tech.hombre.data.repository.myworkspaces

import tech.hombre.data.networking.NOT_HAVE_INTERNET_CONNECTION
import tech.hombre.data.networking.WorkspacesApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.query
import tech.hombre.data.networking.model.ReviewBody
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.Failure
import tech.hombre.domain.model.HttpError
import tech.hombre.domain.model.Result
import tech.hombre.domain.model.ReviewGrades
import tech.hombre.domain.repository.WorkspaceReviewRepository

class WorkspaceReviewRepositoryImpl(
    private val workspaceApi: WorkspacesApi
) : BaseRepository<Boolean, DomainMapper<Boolean>>(),
    WorkspaceReviewRepository {

    override suspend fun reviewWorkspace(
        workspaceId: Int,
        grades: ReviewGrades,
        review: String
    ): Result<Boolean> =
        if (hasNetworkAccess()) workspaceApi.review(workspaceId, ReviewBody(grades, review)).query() else Failure(
            HttpError(Throwable(NOT_HAVE_INTERNET_CONNECTION))
        )
}
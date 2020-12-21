package tech.hombre.data.repository.feedlist

import tech.hombre.data.networking.FeedApi
import tech.hombre.data.networking.NOT_HAVE_INTERNET_CONNECTION
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.query
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.Failure
import tech.hombre.domain.model.HttpError
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.MarkFeedAsReadRepository

class MarkFeedAsReadRepositoryImpl(
    private val feedApi: FeedApi
) : BaseRepository<Boolean, DomainMapper<Boolean>>(),
    MarkFeedAsReadRepository {

    override suspend fun markFeedAsRead(): Result<Boolean> =
        if (hasNetworkAccess()) feedApi.markFeedAsRead().query() else Failure(HttpError(Throwable(
            NOT_HAVE_INTERNET_CONNECTION
        )))

}

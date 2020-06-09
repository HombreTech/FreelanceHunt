package tech.hombre.data.repository.feedlist

import retrofit2.awaitResponse
import tech.hombre.data.networking.FeedApi
import tech.hombre.data.networking.GENERAL_NETWORK_ERROR
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.Failure
import tech.hombre.domain.model.HttpError
import tech.hombre.domain.model.Result
import tech.hombre.domain.model.Success
import tech.hombre.domain.repository.MarkFeedAsReadRepository

class MarkFeedAsReadRepositoryImpl(
    private val feedApi: FeedApi
) : BaseRepository<Boolean, DomainMapper<Boolean>>(),
    MarkFeedAsReadRepository {

    override suspend fun markFeedAsRead(): Result<Boolean> {
        return if (feedApi.markFeedAsRead().isSuccessful
        ) Success(true) else Failure(
            HttpError(Throwable(GENERAL_NETWORK_ERROR))
        )
    }
}

package tech.hombre.domain.repository

import tech.hombre.domain.model.FeedList
import tech.hombre.domain.model.Result

interface FeedListRepository {
    suspend fun getFeedList(): Result<FeedList>
}
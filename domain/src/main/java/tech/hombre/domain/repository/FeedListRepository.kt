package tech.hombre.domain.repository

import tech.hombre.domain.model.FeedList
import tech.hombre.domain.model.Result

interface FeedListRepository {
    suspend fun getFeedList(url: String): Result<FeedList>
}
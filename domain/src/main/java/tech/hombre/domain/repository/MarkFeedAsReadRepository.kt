package tech.hombre.domain.repository

import tech.hombre.domain.model.Result

interface MarkFeedAsReadRepository {

    suspend fun markFeedAsRead(): Result<Boolean>
}
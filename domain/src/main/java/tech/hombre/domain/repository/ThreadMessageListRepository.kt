package tech.hombre.domain.repository

import tech.hombre.domain.model.Result
import tech.hombre.domain.model.ThreadMessageList

interface ThreadMessageListRepository {
    suspend fun getThreadMessages(threadId: Int): Result<ThreadMessageList>
}
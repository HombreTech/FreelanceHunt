package tech.hombre.domain.repository

import tech.hombre.domain.model.Result
import tech.hombre.domain.model.ThreadMessageList

interface SendThreadMessageRepository {
    suspend fun sendThreadMessage(threadId: Int, message: String): Result<ThreadMessageList.Data>
}
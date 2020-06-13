package tech.hombre.domain.interaction.threadslist.messages

import tech.hombre.domain.model.Result
import tech.hombre.domain.model.ThreadMessageList

interface SendThreadMessageUseCase {

    suspend operator fun invoke(threadId: Int, message: String): Result<ThreadMessageList.Data>
}
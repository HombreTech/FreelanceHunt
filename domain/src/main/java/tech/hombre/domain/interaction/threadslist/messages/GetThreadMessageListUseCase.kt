package tech.hombre.domain.interaction.threadslist.messages

import tech.hombre.domain.base.BaseUseCase
import tech.hombre.domain.model.Result
import tech.hombre.domain.model.ThreadMessageList

interface GetThreadMessageListUseCase : BaseUseCase<Int, ThreadMessageList> {

    override suspend operator fun invoke(threadId: Int): Result<ThreadMessageList>
}
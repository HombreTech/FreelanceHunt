package tech.hombre.domain.interaction.threadslist.messages

import tech.hombre.domain.repository.ThreadMessageListRepository

class GetThreadMessageListUseCaseImpl(private val threadsMessageListRepository: ThreadMessageListRepository) :
    GetThreadMessageListUseCase {

    override suspend operator fun invoke(threadId: Int) =
        threadsMessageListRepository.getThreadMessages(threadId)
}
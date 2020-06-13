package tech.hombre.domain.interaction.threadslist.messages

import tech.hombre.domain.repository.SendThreadMessageRepository

class SendThreadMessageUseCaseImpl(private val threadsMessageListRepository: SendThreadMessageRepository) :
    SendThreadMessageUseCase {

    override suspend operator fun invoke(threadId: Int, message: String) =
        threadsMessageListRepository.sendThreadMessage(threadId, message)
}
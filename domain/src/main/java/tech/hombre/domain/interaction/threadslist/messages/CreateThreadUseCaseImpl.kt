package tech.hombre.domain.interaction.threadslist.messages

import tech.hombre.domain.repository.CreateThreadRepository

class CreateThreadUseCaseImpl(private val threadsMessageListRepository: CreateThreadRepository) :
    CreateThreadUseCase {

    override suspend operator fun invoke(profileId: Int, subject: String, message: String) = threadsMessageListRepository.createThread(profileId, subject, message)
}
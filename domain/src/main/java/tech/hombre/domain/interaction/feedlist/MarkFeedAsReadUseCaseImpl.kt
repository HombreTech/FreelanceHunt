package tech.hombre.domain.interaction.feedlist

import tech.hombre.domain.repository.MarkFeedAsReadRepository

class MarkFeedAsReadUseCaseImpl(private val markRepository: MarkFeedAsReadRepository) :
    MarkFeedAsReadUseCase {

    override suspend operator fun invoke() = markRepository.markFeedAsRead()


}
package tech.hombre.domain.interaction.feedlist

import tech.hombre.domain.repository.FeedListRepository

class GetFeedListUseCaseImpl(private val feedlistRepository: FeedListRepository) :
    GetFeedListUseCase {

    override suspend operator fun invoke(url: String) = feedlistRepository.getFeedList(url)
}
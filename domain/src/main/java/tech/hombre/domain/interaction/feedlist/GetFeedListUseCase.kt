package tech.hombre.domain.interaction.feedlist

import tech.hombre.domain.base.BaseUseCase
import tech.hombre.domain.model.FeedList
import tech.hombre.domain.model.Result

interface GetFeedListUseCase : BaseUseCase<String, FeedList> {

    override suspend operator fun invoke(url: String): Result<FeedList>
}
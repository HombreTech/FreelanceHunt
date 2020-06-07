package tech.hombre.data.networking.model

import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.FeedList

data class FeedListSimpleResponse(
    val data: List<FeedList.Data>,
    val links: FeedList.Links
) : DomainMapper<FeedList> {

    override fun mapToDomainModel() = FeedList(data, links)
}


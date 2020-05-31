package tech.hombre.data.networking.model

import tech.hombre.data.database.model.FeedListEntity
import tech.hombre.data.networking.base.RoomMapper
import tech.hombre.domain.model.FeedList

data class FeedListResponse(
    val data: List<FeedList.Data>,
    val links: FeedList.Links
) : RoomMapper<FeedListEntity> {

    override fun mapToRoomEntity() =
        FeedListEntity(url = links.self, feedList = FeedList(data, links))
}


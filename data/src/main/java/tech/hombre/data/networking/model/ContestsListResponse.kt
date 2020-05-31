package tech.hombre.data.networking.model

import tech.hombre.data.database.model.ContestsListEntity
import tech.hombre.data.networking.base.RoomMapper
import tech.hombre.domain.model.ContestsList

data class ContestsListResponse(
    val data: List<ContestsList.Data>,
    val links: ContestsList.Links
) : RoomMapper<ContestsListEntity> {

    override fun mapToRoomEntity() =
        ContestsListEntity(url = links.self, contestsList = ContestsList(data, links))
}


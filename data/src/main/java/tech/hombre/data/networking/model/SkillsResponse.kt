package tech.hombre.data.networking.model

import tech.hombre.data.database.model.SkillsEntity
import tech.hombre.data.networking.base.RoomMapper
import tech.hombre.domain.model.SkillList

data class SkillsResponse(
    val data: List<SkillList.Data>,
    val links: SkillList.Links
) : RoomMapper<SkillsEntity> {

    override fun mapToRoomEntity() = SkillsEntity(skills = SkillList(data, links))
}


package tech.hombre.data.networking.model

import tech.hombre.data.database.model.CitiesEntity
import tech.hombre.data.networking.base.RoomMapper
import tech.hombre.domain.model.Cities

data class CitiesResponse(
    val data: List<Cities.Data>,
    val countryId: Int,
    val links: Cities.Links
) : RoomMapper<CitiesEntity> {

    override fun mapToRoomEntity() = CitiesEntity(cities = Cities(data, links), countryId = countryId)
}


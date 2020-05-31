package tech.hombre.data.networking.model

import tech.hombre.data.database.model.CountriesEntity
import tech.hombre.data.networking.base.RoomMapper
import tech.hombre.domain.model.Countries

data class CountriesResponse(
    val data: List<Countries.Data>,
    val links: Countries.Links
) : RoomMapper<CountriesEntity> {

    override fun mapToRoomEntity() = CountriesEntity(countries = Countries(data, links))
}


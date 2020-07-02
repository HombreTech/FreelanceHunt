package tech.hombre.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import tech.hombre.data.database.CITIES_TABLE_NAME
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.Cities

@Entity(tableName = CITIES_TABLE_NAME)
data class CitiesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var countryId: Int,
    val cities: Cities
) : DomainMapper<Cities> {

    override fun mapToDomainModel() = cities ?: Cities()
}

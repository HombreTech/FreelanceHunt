package tech.hombre.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import tech.hombre.data.database.COUNTRIES_TABLE_NAME
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.Countries

@Entity(tableName = COUNTRIES_TABLE_NAME)
data class CountriesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val countries: Countries
) : DomainMapper<Countries> {

    override fun mapToDomainModel() = countries ?: Countries()
}

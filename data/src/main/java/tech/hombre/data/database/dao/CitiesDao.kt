package tech.hombre.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tech.hombre.data.database.CITIES_TABLE_NAME
import tech.hombre.data.database.model.CitiesEntity

@Dao
interface CitiesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCities(cities: CitiesEntity)

    @Query("SELECT * FROM $CITIES_TABLE_NAME WHERE countryId = :countryId ORDER BY id DESC LIMIT 1")
    suspend fun getCities(countryId: Int): CitiesEntity
}
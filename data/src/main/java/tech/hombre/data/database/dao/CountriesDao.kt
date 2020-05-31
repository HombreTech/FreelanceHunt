package tech.hombre.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tech.hombre.data.database.COUNTRIES_TABLE_NAME
import tech.hombre.data.database.model.CountriesEntity

@Dao
interface CountriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCountries(countries: CountriesEntity)

    @Query("SELECT * FROM $COUNTRIES_TABLE_NAME ORDER BY id DESC LIMIT 1")
    suspend fun getCountries(): CountriesEntity
}
package tech.hombre.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import tech.hombre.data.database.dao.CountriesDao
import tech.hombre.data.database.model.CountriesEntity

@Database(entities = [CountriesEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CountriesDatabase : RoomDatabase() {

    abstract fun countriesDao(): CountriesDao
}
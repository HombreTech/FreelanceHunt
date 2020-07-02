package tech.hombre.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import tech.hombre.data.database.dao.CitiesDao
import tech.hombre.data.database.model.CitiesEntity

@Database(entities = [CitiesEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CitiesDatabase : RoomDatabase() {

    abstract fun citiesDao(): CitiesDao
}
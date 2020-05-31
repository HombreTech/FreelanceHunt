package tech.hombre.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import tech.hombre.data.database.dao.ContestsListDao
import tech.hombre.data.database.model.ContestsListEntity

@Database(entities = [ContestsListEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ContestsListDatabase : RoomDatabase() {

    abstract fun contestsListDao(): ContestsListDao
}
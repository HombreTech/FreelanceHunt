package tech.hombre.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import tech.hombre.data.database.dao.FeedListDao
import tech.hombre.data.database.model.FeedListEntity

@Database(entities = [FeedListEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FeedListDatabase : RoomDatabase() {

    abstract fun feedListDao(): FeedListDao
}
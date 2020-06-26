package tech.hombre.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import tech.hombre.data.database.dao.SkillsDao
import tech.hombre.data.database.model.SkillsEntity

@Database(entities = [SkillsEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class SkillsDatabase : RoomDatabase() {

    abstract fun skillsDao(): SkillsDao
}
package tech.hombre.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import tech.hombre.data.database.dao.ProjectsListDao
import tech.hombre.data.database.model.ProjectsListEntity

@Database(entities = [ProjectsListEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ProjectsListDatabase : RoomDatabase() {

    abstract fun projectsListDao(): ProjectsListDao
}
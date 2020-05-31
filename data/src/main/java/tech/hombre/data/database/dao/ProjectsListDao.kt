package tech.hombre.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tech.hombre.data.database.PROJECTS_LIST_TABLE_NAME
import tech.hombre.data.database.model.ProjectsListEntity

@Dao
interface ProjectsListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProjectsList(projectsList: ProjectsListEntity)

    @Query("SELECT * FROM $PROJECTS_LIST_TABLE_NAME WHERE url = :url ORDER BY id DESC LIMIT 1")
    suspend fun getProjectsList(url: String): ProjectsListEntity
}
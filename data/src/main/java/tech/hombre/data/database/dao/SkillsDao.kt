package tech.hombre.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tech.hombre.data.database.SKILLS_TABLE_NAME
import tech.hombre.data.database.model.SkillsEntity

@Dao
interface SkillsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSkills(skills: SkillsEntity)

    @Query("SELECT * FROM $SKILLS_TABLE_NAME ORDER BY id DESC LIMIT 1")
    suspend fun getSkills(): SkillsEntity
}
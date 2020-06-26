package tech.hombre.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import tech.hombre.data.database.SKILLS_TABLE_NAME
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.ProjectDetail
import tech.hombre.domain.model.SkillList

@Entity(tableName = SKILLS_TABLE_NAME)
data class SkillsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val skills: SkillList
) : DomainMapper<SkillList> {

    override fun mapToDomainModel() = skills ?: SkillList()
}

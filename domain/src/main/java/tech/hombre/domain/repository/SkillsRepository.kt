package tech.hombre.domain.repository

import tech.hombre.domain.model.Result
import tech.hombre.domain.model.SkillList

interface SkillsRepository {
    suspend fun getSkills(): Result<SkillList>
}
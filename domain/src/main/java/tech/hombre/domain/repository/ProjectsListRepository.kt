package tech.hombre.domain.repository

import tech.hombre.domain.model.ProjectsList
import tech.hombre.domain.model.Result

interface ProjectsListRepository {
    suspend fun getProjectsList(url: String, onlyMySkills: Boolean, onlyForPlus: Boolean, skills: String, employerId: Int): Result<ProjectsList>
}
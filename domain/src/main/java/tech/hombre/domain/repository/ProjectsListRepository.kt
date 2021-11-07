package tech.hombre.domain.repository

import tech.hombre.domain.model.ProjectsList
import tech.hombre.domain.model.Result

interface ProjectsListRepository {

    suspend fun getProjectsList(page: Int, onlyMySkills: Boolean, onlyForPlus: Boolean, skills: String, employerId: Int?): Result<ProjectsList>

    suspend fun getSimpleProjectsList(page: Int, onlyMySkills: Boolean, onlyForPlus: Boolean, skills: String, employerId: Int?): Result<ProjectsList>
}
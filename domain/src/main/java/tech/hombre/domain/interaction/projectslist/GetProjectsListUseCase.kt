package tech.hombre.domain.interaction.projectslist

import tech.hombre.domain.model.ProjectsList
import tech.hombre.domain.model.Result

interface GetProjectsListUseCase {

    suspend operator fun invoke(page: Int, onlyMySkills: Boolean, onlyForPlus: Boolean, skills: String, employerId: Int?): Result<ProjectsList>
}
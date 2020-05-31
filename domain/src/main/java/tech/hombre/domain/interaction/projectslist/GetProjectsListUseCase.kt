package tech.hombre.domain.interaction.projectslist

import tech.hombre.domain.base.BaseUseCase
import tech.hombre.domain.model.ProjectsList
import tech.hombre.domain.model.Result

interface GetProjectsListUseCase : BaseUseCase<String, ProjectsList> {

    override suspend operator fun invoke(url: String): Result<ProjectsList>
}
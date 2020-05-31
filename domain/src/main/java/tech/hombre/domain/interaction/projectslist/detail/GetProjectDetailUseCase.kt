package tech.hombre.domain.interaction.projectslist.detail

import tech.hombre.domain.base.BaseUseCase
import tech.hombre.domain.model.ProjectDetail
import tech.hombre.domain.model.Result

interface GetProjectDetailUseCase : BaseUseCase<String, ProjectDetail> {

    override suspend operator fun invoke(url: String): Result<ProjectDetail>
}
package tech.hombre.domain.interaction.projectslist.comments

import tech.hombre.domain.base.BaseUseCase
import tech.hombre.domain.model.ProjectComment
import tech.hombre.domain.model.Result

interface GetProjectCommentsUseCase : BaseUseCase<Int, ProjectComment> {

    override suspend operator fun invoke(projectId: Int): Result<ProjectComment>
}
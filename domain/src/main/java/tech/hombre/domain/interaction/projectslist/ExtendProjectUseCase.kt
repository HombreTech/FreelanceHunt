package tech.hombre.domain.interaction.projectslist

import tech.hombre.domain.model.ProjectDetail
import tech.hombre.domain.model.Result

interface ExtendProjectUseCase {

    suspend operator fun invoke(projectId: Int,
                                expiredAt: String): Result<ProjectDetail>
}
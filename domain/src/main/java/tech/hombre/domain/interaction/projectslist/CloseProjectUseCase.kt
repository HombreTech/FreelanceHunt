package tech.hombre.domain.interaction.projectslist

import tech.hombre.domain.model.Result

interface CloseProjectUseCase {

    suspend operator fun invoke(projectId: Int): Result<Boolean>
}
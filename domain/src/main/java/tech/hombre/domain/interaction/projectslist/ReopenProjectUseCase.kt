package tech.hombre.domain.interaction.projectslist

import tech.hombre.domain.model.Result

interface ReopenProjectUseCase {

    suspend operator fun invoke(projectId: Int,
                                expiredAt: String): Result<Boolean>
}
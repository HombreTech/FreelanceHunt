package tech.hombre.domain.interaction.myworkspaces

import tech.hombre.domain.model.Result

interface WorkspaceCloseUseCase {

    suspend operator fun invoke(
        workspaceId: Int,
        review: String
    ): Result<Boolean>
}
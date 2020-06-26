package tech.hombre.domain.interaction.myworkspaces

import tech.hombre.domain.model.Result

interface RejectConditionsUseCase {

    suspend operator fun invoke(
        workspaceId: Int
    ): Result<Boolean>
}
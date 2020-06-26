package tech.hombre.domain.interaction.myworkspaces

import tech.hombre.domain.model.Result

interface AcceptConditionsUseCase {

    suspend operator fun invoke(
        workspaceId: Int
    ): Result<Boolean>
}
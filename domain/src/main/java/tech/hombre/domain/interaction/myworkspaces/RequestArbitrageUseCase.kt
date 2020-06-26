package tech.hombre.domain.interaction.myworkspaces

import tech.hombre.domain.model.Result

interface RequestArbitrageUseCase {

    suspend operator fun invoke(
        workspaceId: Int,
        comment: String
    ): Result<Boolean>
}
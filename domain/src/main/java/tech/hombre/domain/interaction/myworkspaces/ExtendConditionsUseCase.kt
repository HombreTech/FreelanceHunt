package tech.hombre.domain.interaction.myworkspaces

import tech.hombre.domain.model.Result
import tech.hombre.domain.model.Workspace

interface ExtendConditionsUseCase {

    suspend operator fun invoke(
        workspaceId: Int, days: Int
    ): Result<Workspace>
}
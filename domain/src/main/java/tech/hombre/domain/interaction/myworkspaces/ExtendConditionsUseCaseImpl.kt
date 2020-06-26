package tech.hombre.domain.interaction.myworkspaces

import tech.hombre.domain.model.Result
import tech.hombre.domain.model.Workspace
import tech.hombre.domain.repository.ExtendConditionsRepository

class ExtendConditionsUseCaseImpl(private val workspaceslistRepository: ExtendConditionsRepository) :
    ExtendConditionsUseCase {

    override suspend operator fun invoke(
        workspaceId: Int,
        days: Int
    ): Result<Workspace> =
        workspaceslistRepository.extendConditions(workspaceId, days)
}
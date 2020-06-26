package tech.hombre.domain.interaction.myworkspaces

import tech.hombre.domain.repository.RejectConditionsRepository

class RejectConditionsUseCaseImpl(private val workspaceslistRepository: RejectConditionsRepository) :
    RejectConditionsUseCase {

    override suspend operator fun invoke(
        workspaceId: Int
    ) = workspaceslistRepository.rejectConditions(workspaceId)
}
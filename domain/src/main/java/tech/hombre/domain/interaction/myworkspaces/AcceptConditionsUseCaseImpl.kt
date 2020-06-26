package tech.hombre.domain.interaction.myworkspaces

import tech.hombre.domain.repository.AcceptConditionsRepository

class AcceptConditionsUseCaseImpl(private val workspaceslistRepository: AcceptConditionsRepository) :
    AcceptConditionsUseCase {

    override suspend operator fun invoke(
        workspaceId: Int
    ) = workspaceslistRepository.acceptConditions(workspaceId)
}
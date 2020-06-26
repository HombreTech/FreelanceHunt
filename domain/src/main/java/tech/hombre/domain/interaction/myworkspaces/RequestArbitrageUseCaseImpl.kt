package tech.hombre.domain.interaction.myworkspaces

import tech.hombre.domain.repository.RequestArbitrageRepository

class RequestArbitrageUseCaseImpl(private val workspaceslistRepository: RequestArbitrageRepository) :
    RequestArbitrageUseCase {

    override suspend operator fun invoke(
        workspaceId: Int,
        comment: String
    ) = workspaceslistRepository.requestArbitrage(workspaceId, comment)
}
package tech.hombre.domain.interaction.myworkspaces

import tech.hombre.domain.repository.MyWorkspacesListRepository

class GetMyWorkspacesListUseCaseImpl(private val workspaceslistRepository: MyWorkspacesListRepository) :
    GetMyWorkspacesListUseCase {

    override suspend operator fun invoke(url: String) = workspaceslistRepository.getMyWorkspacesList(url)
}
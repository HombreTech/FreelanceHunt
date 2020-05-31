package tech.hombre.domain.interaction.projectslist.comments

import tech.hombre.domain.repository.ProjectCommentsRepository

class GetProjectCommentsUseCaseImpl(private val projectCommentsRepository: ProjectCommentsRepository) :
    GetProjectCommentsUseCase {

    override suspend operator fun invoke(projectId: Int) =
        projectCommentsRepository.getProjectComments(projectId)
}
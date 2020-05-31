package tech.hombre.domain.interaction.contests.comments

import tech.hombre.domain.repository.ContestCommentsRepository

class GetContestCommentsUseCaseImpl(private val contestCommentsRepository: ContestCommentsRepository) :
    GetContestCommentsUseCase {

    override suspend operator fun invoke(contestId: Int) =
        contestCommentsRepository.getContestComments(contestId)
}
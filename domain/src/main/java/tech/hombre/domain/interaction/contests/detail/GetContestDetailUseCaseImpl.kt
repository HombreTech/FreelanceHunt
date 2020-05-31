package tech.hombre.domain.interaction.contests.detail

import tech.hombre.domain.repository.ContestDetailRepository

class GetContestDetailUseCaseImpl(private val freelancersRepository: ContestDetailRepository) :
    GetContestDetailUseCase {

    override suspend operator fun invoke(contestId: Int) =
        freelancersRepository.getContestDetail(contestId)
}
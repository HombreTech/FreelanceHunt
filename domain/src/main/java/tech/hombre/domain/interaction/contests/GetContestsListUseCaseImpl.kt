package tech.hombre.domain.interaction.contests

import tech.hombre.domain.repository.ContestsListRepository

class GetContestsListUseCaseImpl(private val contestslistRepository: ContestsListRepository) :
    GetContestsListUseCase {

    override suspend operator fun invoke(page: Int, skills: String?) = contestslistRepository.getContestsList(page, skills)
}
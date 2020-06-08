package tech.hombre.domain.interaction.mycontests

import tech.hombre.domain.repository.MyContestsListRepository

class GetMyContestsListUseCaseImpl(private val contestslistRepository: MyContestsListRepository) :
    GetMyContestsListUseCase {

    override suspend operator fun invoke(url: String) = contestslistRepository.getMyContestsList(url)
}
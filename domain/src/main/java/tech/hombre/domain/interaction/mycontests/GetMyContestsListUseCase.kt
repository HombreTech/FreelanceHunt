package tech.hombre.domain.interaction.mycontests

import tech.hombre.domain.base.BaseUseCase
import tech.hombre.domain.model.MyContestsList
import tech.hombre.domain.model.Result

interface GetMyContestsListUseCase : BaseUseCase<String, MyContestsList> {

    override suspend operator fun invoke(url: String): Result<MyContestsList>
}
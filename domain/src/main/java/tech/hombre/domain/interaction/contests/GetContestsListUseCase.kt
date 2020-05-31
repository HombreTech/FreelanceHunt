package tech.hombre.domain.interaction.contests

import tech.hombre.domain.base.BaseUseCase
import tech.hombre.domain.model.ContestsList
import tech.hombre.domain.model.Result

interface GetContestsListUseCase : BaseUseCase<String, ContestsList> {

    override suspend operator fun invoke(url: String): Result<ContestsList>
}
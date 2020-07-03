package tech.hombre.domain.interaction.contests

import tech.hombre.domain.model.ContestsList
import tech.hombre.domain.model.Result

interface GetContestsListUseCase {

    suspend operator fun invoke(page: Int, skills: String): Result<ContestsList>
}
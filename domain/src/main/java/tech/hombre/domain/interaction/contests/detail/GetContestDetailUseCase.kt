package tech.hombre.domain.interaction.contests.detail

import tech.hombre.domain.base.BaseUseCase
import tech.hombre.domain.model.ContestDetail
import tech.hombre.domain.model.Result

interface GetContestDetailUseCase : BaseUseCase<Int, ContestDetail> {

    override suspend operator fun invoke(contestId: Int): Result<ContestDetail>
}
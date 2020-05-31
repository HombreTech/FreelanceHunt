package tech.hombre.domain.interaction.contests.comments

import tech.hombre.domain.base.BaseUseCase
import tech.hombre.domain.model.ContestComment
import tech.hombre.domain.model.Result

interface GetContestCommentsUseCase : BaseUseCase<Int, ContestComment> {

    override suspend operator fun invoke(contestId: Int): Result<ContestComment>
}
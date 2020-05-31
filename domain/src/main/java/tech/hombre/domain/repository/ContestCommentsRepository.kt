package tech.hombre.domain.repository

import tech.hombre.domain.model.ContestComment
import tech.hombre.domain.model.Result

interface ContestCommentsRepository {
    suspend fun getContestComments(contestId: Int): Result<ContestComment>
}
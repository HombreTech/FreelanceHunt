package tech.hombre.domain.repository

import tech.hombre.domain.model.ContestDetail
import tech.hombre.domain.model.Result

interface ContestDetailRepository {
    suspend fun getContestDetail(contestId: Int): Result<ContestDetail>
}
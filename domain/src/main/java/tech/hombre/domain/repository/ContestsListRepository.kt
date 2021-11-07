package tech.hombre.domain.repository

import tech.hombre.domain.model.ContestsList
import tech.hombre.domain.model.Result

interface ContestsListRepository {
    suspend fun getContestsList(page: Int, skills: String?): Result<ContestsList>
}
package tech.hombre.domain.repository

import tech.hombre.domain.model.MyContestsList
import tech.hombre.domain.model.Result

interface MyContestsListRepository {
    suspend fun getMyContestsList(url: String): Result<MyContestsList>
}
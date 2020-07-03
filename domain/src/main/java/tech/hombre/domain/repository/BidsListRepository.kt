package tech.hombre.domain.repository

import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.Result

interface BidsListRepository {
    suspend fun getMyBidsList(page: Int, status: String): Result<MyBidsList>
}
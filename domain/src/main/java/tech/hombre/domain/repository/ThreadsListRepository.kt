package tech.hombre.domain.repository

import tech.hombre.domain.model.Result
import tech.hombre.domain.model.ThreadsList

interface ThreadsListRepository {
    suspend fun getThreadsList(url: String): Result<ThreadsList>
}
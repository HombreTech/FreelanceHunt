package tech.hombre.domain.repository

import tech.hombre.domain.model.Result
import tech.hombre.domain.model.ThreadsList

interface CreateThreadRepository {
    suspend fun createThread(profileId: Int, subject: String, message: String): Result<ThreadsList.Data>
}
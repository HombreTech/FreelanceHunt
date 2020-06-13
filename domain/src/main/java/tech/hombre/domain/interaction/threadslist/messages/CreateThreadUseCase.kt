package tech.hombre.domain.interaction.threadslist.messages

import tech.hombre.domain.model.Result
import tech.hombre.domain.model.ThreadsList

interface CreateThreadUseCase {

    suspend operator fun invoke(profileId: Int, subject: String, message: String): Result<ThreadsList.Data>
}
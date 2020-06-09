package tech.hombre.domain.interaction.feedlist

import tech.hombre.domain.model.Result

interface MarkFeedAsReadUseCase {

    suspend operator fun invoke(): Result<Boolean>
}
package tech.hombre.domain.interaction.threadslist

import tech.hombre.domain.base.BaseUseCase
import tech.hombre.domain.model.Result
import tech.hombre.domain.model.ThreadsList

interface GetThreadsListUseCase : BaseUseCase<String, ThreadsList> {

    override suspend operator fun invoke(url: String): Result<ThreadsList>
}
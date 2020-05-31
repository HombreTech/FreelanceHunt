package tech.hombre.domain.interaction.threadslist

import tech.hombre.domain.repository.ThreadsListRepository

class GetThreadsListUseCaseImpl(private val threadsListRepository: ThreadsListRepository) :
    GetThreadsListUseCase {

    override suspend operator fun invoke(url: String) = threadsListRepository.getThreadsList(url)
}
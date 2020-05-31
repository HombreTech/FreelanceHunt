package tech.hombre.data.repository.threadslist

import tech.hombre.data.networking.ThreadsApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.getData
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.Result
import tech.hombre.domain.model.ThreadMessageList
import tech.hombre.domain.repository.ThreadMessageListRepository

class ThreadMessageListRepositoryImpl(
    private val threadsApi: ThreadsApi
) : BaseRepository<ThreadMessageList, DomainMapper<ThreadMessageList>>(),
    ThreadMessageListRepository {
    override suspend fun getThreadMessages(threadId: Int): Result<ThreadMessageList> {
        return fetchData(
            dataProvider = { threadsApi.getThreadMessagesList(threadId).getData() }
        )
    }
}
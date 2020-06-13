package tech.hombre.data.repository.threadslist

import tech.hombre.data.networking.ThreadsApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.getData
import tech.hombre.data.networking.model.SendMessageBody
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.Result
import tech.hombre.domain.model.ThreadMessageList
import tech.hombre.domain.repository.SendThreadMessageRepository
import tech.hombre.domain.repository.ThreadMessageListRepository

class SendThreadMessageRepositoryImpl(
    private val threadsApi: ThreadsApi
) : BaseRepository<ThreadMessageList.Data, DomainMapper<ThreadMessageList.Data>>(),
    SendThreadMessageRepository {
    override suspend fun sendThreadMessage(threadId: Int, message: String): Result<ThreadMessageList.Data> {
        return fetchData(
            dataProvider = { threadsApi.sendThreadMessage(threadId, SendMessageBody(message)).getData() }
        )
    }
}
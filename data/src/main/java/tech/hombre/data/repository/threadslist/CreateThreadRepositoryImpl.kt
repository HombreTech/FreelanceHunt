package tech.hombre.data.repository.threadslist

import tech.hombre.data.networking.ThreadsApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.getData
import tech.hombre.data.networking.model.CreateThreadBody
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.Result
import tech.hombre.domain.model.ThreadsList
import tech.hombre.domain.repository.CreateThreadRepository

class CreateThreadRepositoryImpl(
    private val threadsApi: ThreadsApi
) : BaseRepository<ThreadsList.Data, DomainMapper<ThreadsList.Data>>(),
    CreateThreadRepository {
    override suspend fun createThread(
        profileId: Int,
        subject: String,
        message: String
    ): Result<ThreadsList.Data> {
        return fetchData(
            dataProvider = {
                threadsApi.createThreadMessage(
                    CreateThreadBody(
                        subject,
                        message,
                        profileId
                    )
                ).getData()
            }
        )
    }
}
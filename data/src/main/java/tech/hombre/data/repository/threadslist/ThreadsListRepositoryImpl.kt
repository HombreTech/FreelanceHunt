package tech.hombre.data.repository.threadslist

import tech.hombre.data.networking.ThreadsApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.getData
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.Result
import tech.hombre.domain.model.ThreadsList
import tech.hombre.domain.repository.ThreadsListRepository

class ThreadsListRepositoryImpl(
    private val threadsApi: ThreadsApi
) : BaseRepository<ThreadsList, DomainMapper<ThreadsList>>(),
    ThreadsListRepository {
    override suspend fun getThreadsList(url: String): Result<ThreadsList> {
        return fetchData(
            dataProvider = { threadsApi.getThreadsList(url).getData() }
        )
    }
}
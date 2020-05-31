package tech.hombre.data.repository.contestslist

import tech.hombre.data.networking.ContestsApi
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.data.networking.base.getData
import tech.hombre.data.repository.BaseRepository
import tech.hombre.domain.model.ContestComment
import tech.hombre.domain.model.Result
import tech.hombre.domain.repository.ContestCommentsRepository

class ContestCommentsRepositoryImpl(
    private val contestApi: ContestsApi
) : BaseRepository<ContestComment, DomainMapper<ContestComment>>(),
    ContestCommentsRepository {

    override suspend fun getContestComments(contestId: Int): Result<ContestComment> {
        return fetchData(
            dataProvider = { contestApi.getContestsComments(contestId).getData() }
        )
    }
}
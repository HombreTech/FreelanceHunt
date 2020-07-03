package tech.hombre.domain.interaction.mybids

import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.Result

interface GetMyBidsListUseCase {

    suspend operator fun invoke(page: Int, status: String): Result<MyBidsList>
}
package tech.hombre.domain.interaction.mybids

import tech.hombre.domain.base.BaseUseCase
import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.Result

interface GetMyBidsListUseCase : BaseUseCase<String, MyBidsList> {

    override suspend operator fun invoke(url: String): Result<MyBidsList>
}
package tech.hombre.domain.interaction.mybids

import tech.hombre.domain.repository.BidsListRepository

class GetMyBidsListUseCaseImpl(private val mybidsListRepository: BidsListRepository) :
    GetMyBidsListUseCase {

    override suspend operator fun invoke(url: String) = mybidsListRepository.getMyBidsList(url)
}
package tech.hombre.freelancehunt.ui.my.bids.presentation

import tech.hombre.domain.interaction.mybids.GetMyBidsListUseCase
import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success

class MyBidsViewModel(private val getMyBidsList: GetMyBidsListUseCase) :
    BaseViewModel<MyBidsList>() {

    lateinit var pagination: MyBidsList.Links

    fun getMyBids(url: String = "my/bids") = executeUseCase {
        getMyBidsList(url)
            .onSuccess {
                pagination = it.links
                _viewState.value = Success(it)
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }
}

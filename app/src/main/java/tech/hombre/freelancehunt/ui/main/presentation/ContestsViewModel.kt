package tech.hombre.freelancehunt.ui.main.presentation

import tech.hombre.data.BuildConfig
import tech.hombre.domain.interaction.contests.GetContestsListUseCase
import tech.hombre.domain.model.ContestsList
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success

class ContestsViewModel(private val getContestsList: GetContestsListUseCase) :
    BaseViewModel<ContestsList>() {

    lateinit var pagination: ContestsList.Links

    fun getContestsLists(url: String = BuildConfig.BASE_URL + "contests") = executeUseCase {
        getContestsList(url)
            .onSuccess {
                pagination = it.links
                _viewState.value = Success(it)
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }
}

package tech.hombre.freelancehunt.ui.main.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tech.hombre.data.BuildConfig
import tech.hombre.domain.interaction.contests.GetContestsListUseCase
import tech.hombre.domain.interaction.contests.detail.GetContestDetailUseCase
import tech.hombre.domain.model.ContestDetail
import tech.hombre.domain.model.ContestsList
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success
import tech.hombre.freelancehunt.ui.base.ViewState

class ContestsViewModel(
    private val getContestsList: GetContestsListUseCase,
    private val getContestDetail: GetContestDetailUseCase
) :
    BaseViewModel<ContestsList>() {

    lateinit var pagination: ContestsList.Links

    val _details = MutableLiveData<ViewState<ContestDetail>>()
    val details: LiveData<ViewState<ContestDetail>>
        get() = _details

    fun getContestsLists(url: String = BuildConfig.BASE_URL + "contests") = executeUseCase {
        getContestsList(url)
            .onSuccess {
                pagination = it.links
                _viewState.value = Success(it)
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun getContestDetails(contestId: Int) = executeUseCase {
        getContestDetail(contestId)
            .onSuccess {
                _details.value = Success(it)
            }
            .onFailure { _details.value = Error(it.throwable) }
    }
}

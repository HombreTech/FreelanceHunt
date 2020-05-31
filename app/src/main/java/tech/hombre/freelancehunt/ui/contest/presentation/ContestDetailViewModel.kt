package tech.hombre.freelancehunt.ui.contest.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tech.hombre.data.database.dao.CountriesDao
import tech.hombre.domain.interaction.contests.detail.GetContestDetailUseCase
import tech.hombre.domain.model.ContestDetail
import tech.hombre.domain.model.Countries
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success

class ContestDetailViewModel(
    private val getContestDetail: GetContestDetailUseCase,
    private val countriesDao: CountriesDao
) :
    BaseViewModel<ContestDetail>() {

    val _countries = MutableLiveData<List<Countries.Data>>()
    val countries: LiveData<List<Countries.Data>>
        get() = _countries

    fun getContestDetails(contestId: Int) = executeUseCase {
        getContestDetail(contestId)
            .onSuccess {
                _viewState.value = Success(it)
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun setCountries() = executeUseCase {
        _countries.value = countriesDao.getCountries().countries.data
    }
}

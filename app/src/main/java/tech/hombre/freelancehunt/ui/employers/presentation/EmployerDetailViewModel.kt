package tech.hombre.freelancehunt.ui.employers.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tech.hombre.data.database.dao.CountriesDao
import tech.hombre.domain.interaction.employerslist.detail.GetEmployerDetailUseCase
import tech.hombre.domain.model.Countries
import tech.hombre.domain.model.EmployerDetail
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success

class EmployerDetailViewModel(
    private val getEmployerDetail: GetEmployerDetailUseCase,
    private val countriesDao: CountriesDao
) :
    BaseViewModel<EmployerDetail>() {

    val _countries = MutableLiveData<List<Countries.Data>>()
    val countries: LiveData<List<Countries.Data>>
        get() = _countries

    fun getEmployerDetails(profileId: Int) = executeUseCase {
        getEmployerDetail(profileId)
            .onSuccess {
                _viewState.value = Success(it)
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun setCountries() = executeUseCase {
        _countries.value = countriesDao.getCountries().countries.data
    }
}

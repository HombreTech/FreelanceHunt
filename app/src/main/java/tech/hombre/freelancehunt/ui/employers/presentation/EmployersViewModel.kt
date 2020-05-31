package tech.hombre.freelancehunt.ui.employers.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tech.hombre.data.database.dao.CountriesDao
import tech.hombre.domain.interaction.employerslist.GetEmployersListUseCase
import tech.hombre.domain.model.Countries
import tech.hombre.domain.model.EmployersList
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success

class EmployersViewModel(
    private val getEmployersList: GetEmployersListUseCase,
    private val countriesDao: CountriesDao
) :
    BaseViewModel<EmployersList>() {

    lateinit var pagination: EmployersList.Links

    val _countries = MutableLiveData<List<Countries.Data>>()
    val countries: LiveData<List<Countries.Data>>
        get() = _countries

    fun getEmployers(url: String = "employers") = executeUseCase {
        getEmployersList(url)
            .onSuccess {
                pagination = it.links
                _viewState.value = Success(it)
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun setCountries() = executeUseCase {
        _countries.value = countriesDao.getCountries().countries.data
    }
}

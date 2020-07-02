package tech.hombre.freelancehunt.ui.employers.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tech.hombre.data.database.dao.CountriesDao
import tech.hombre.domain.interaction.employerslist.GetEmployersListUseCase
import tech.hombre.domain.interaction.employerslist.detail.GetEmployerDetailUseCase
import tech.hombre.domain.model.*
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success
import tech.hombre.freelancehunt.ui.base.ViewState

class EmployersViewModel(
    private val getEmployersList: GetEmployersListUseCase,
    private val getEmployerDetail: GetEmployerDetailUseCase,
    private val countriesDao: CountriesDao
) :
    BaseViewModel<EmployersList>() {

    lateinit var pagination: EmployersList.Links

    var countryId = 0

    var cityId = 0

    val _details = MutableLiveData<ViewState<EmployerDetail>>()
    val details: LiveData<ViewState<EmployerDetail>>
        get() = _details

    val _countries = MutableLiveData<List<Countries.Data>>()
    val countries: LiveData<List<Countries.Data>>
        get() = _countries

    fun getEmployers(page: Int) = executeUseCase {
        getEmployersList(page.toString(), countryId, cityId)
            .onSuccess {
                pagination = it.links
                _viewState.value = Success(it)
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun setCountries() = executeUseCase {
        _countries.value = countriesDao.getCountries().countries.data
    }

    fun getEmployerDetails(profileId: Int) = executeUseCase {
        getEmployerDetail(profileId)
            .onSuccess {
                _details.value = Success(it)
            }
            .onFailure { _details.value = Error(it.throwable) }
    }

    fun setEmployersFilters(
        countryId: Int,
        cityId: Int
    ) {
        this.countryId = countryId
        this.cityId = cityId
    }
}

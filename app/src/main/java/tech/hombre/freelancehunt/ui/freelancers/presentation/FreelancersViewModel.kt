package tech.hombre.freelancehunt.ui.freelancers.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tech.hombre.data.database.dao.CountriesDao
import tech.hombre.domain.interaction.freelancerslist.GetFreelancersListUseCase
import tech.hombre.domain.model.Countries
import tech.hombre.domain.model.FreelancersList
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success

class FreelancersViewModel(
    private val getFreelancersList: GetFreelancersListUseCase,
    private val countriesDao: CountriesDao
) :
    BaseViewModel<FreelancersList>() {

    lateinit var pagination: FreelancersList.Links

    var skills = intArrayOf()

    var countryId = 0

    var cityId = 0

    private val _countries = MutableLiveData<List<Countries.Data>>()
    val countries: LiveData<List<Countries.Data>>
        get() = _countries

    fun getFreelancers(page: Int) = executeUseCase {
        getFreelancersList(page.toString(), skills.joinToString(separator = ",") { it.toString() }, countryId, cityId)
            .onSuccess {
                pagination = it.links
                _viewState.value = Success(it)
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun setCountries() = executeUseCase {
        _countries.value = countriesDao.getCountries().countries.data
    }

    fun setFreelancersFilters(
        skills: IntArray,
        countryId: Int,
        cityId: Int
    ) {
        this.skills = skills
        this.countryId = countryId
        this.cityId = cityId
    }

}

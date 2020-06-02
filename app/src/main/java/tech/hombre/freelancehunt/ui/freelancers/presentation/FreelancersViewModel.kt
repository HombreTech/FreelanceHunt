package tech.hombre.freelancehunt.ui.freelancers.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tech.hombre.data.database.dao.CountriesDao
import tech.hombre.domain.interaction.freelancerslist.GetFreelancersListUseCase
import tech.hombre.domain.interaction.freelancerslist.detail.GetFreelancerDetailUseCase
import tech.hombre.domain.model.*
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success
import tech.hombre.freelancehunt.ui.base.ViewState

class FreelancersViewModel(
    private val getFreelancersList: GetFreelancersListUseCase,
    private val getFreelancerDetail: GetFreelancerDetailUseCase,
    private val countriesDao: CountriesDao
) :
    BaseViewModel<FreelancersList>() {

    lateinit var pagination: FreelancersList.Links

    val _details = MutableLiveData<ViewState<FreelancerDetail>>()
    val details: LiveData<ViewState<FreelancerDetail>>
        get() = _details

    private val _countries = MutableLiveData<List<Countries.Data>>()
    val countries: LiveData<List<Countries.Data>>
        get() = _countries

    fun getFreelancers(url: String = "freelancers") = executeUseCase {
        getFreelancersList(url)
            .onSuccess {
                pagination = it.links
                _viewState.value = Success(it)
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun setCountries() = executeUseCase {
        _countries.value = countriesDao.getCountries().countries.data
    }

    fun getFreelancerDetails(profileId: Int) = executeUseCase {
        getFreelancerDetail(profileId)
            .onSuccess {
                _details.value = Success(it)
            }
            .onFailure { _details.value = Error(it.throwable) }
    }
}

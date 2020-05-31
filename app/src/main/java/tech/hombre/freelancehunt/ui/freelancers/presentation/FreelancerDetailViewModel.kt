package tech.hombre.freelancehunt.ui.freelancers.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tech.hombre.data.database.dao.CountriesDao
import tech.hombre.domain.interaction.freelancerslist.detail.GetFreelancerDetailUseCase
import tech.hombre.domain.model.Countries
import tech.hombre.domain.model.FreelancerDetail
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success

class FreelancerDetailViewModel(
    private val getFreelancerDetail: GetFreelancerDetailUseCase,
    private val countriesDao: CountriesDao
) :
    BaseViewModel<FreelancerDetail>() {

    val _countries = MutableLiveData<List<Countries.Data>>()
    val countries: LiveData<List<Countries.Data>>
        get() = _countries

    fun getFreelancerDetails(profileId: Int) = executeUseCase {
        getFreelancerDetail(profileId)
            .onSuccess {
                _viewState.value = Success(it)
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun setCountries() = executeUseCase {
        _countries.value = countriesDao.getCountries().countries.data
    }
}

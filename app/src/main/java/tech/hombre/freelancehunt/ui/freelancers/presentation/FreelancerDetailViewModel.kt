package tech.hombre.freelancehunt.ui.freelancers.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tech.hombre.data.database.dao.CountriesDao
import tech.hombre.domain.interaction.freelancerslist.detail.GetFreelancerDetailUseCase
import tech.hombre.domain.interaction.threadslist.messages.CreateThreadUseCase
import tech.hombre.domain.model.Countries
import tech.hombre.domain.model.FreelancerDetail
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success
import tech.hombre.freelancehunt.ui.base.ViewState

class FreelancerDetailViewModel(
    private val getFreelancerDetail: GetFreelancerDetailUseCase,
    private val countriesDao: CountriesDao,
    private val createThread: CreateThreadUseCase
) :
    BaseViewModel<FreelancerDetail>() {

    val _countries = MutableLiveData<List<Countries.Data>>()
    val countries: LiveData<List<Countries.Data>>
        get() = _countries

    val _action = MutableLiveData<ViewState<String>>()
    val action: LiveData<ViewState<String>>
        get() = _action

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

    fun sendMessage(profileId: Int, subject: String, message: String) = executeUseCase {
        createThread(profileId, subject, message)
            .onSuccess {
                _action.value = Success("message")
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }
}

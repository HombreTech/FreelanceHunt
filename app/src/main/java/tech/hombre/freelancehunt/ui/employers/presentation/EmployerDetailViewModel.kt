package tech.hombre.freelancehunt.ui.employers.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tech.hombre.data.database.dao.CountriesDao
import tech.hombre.domain.interaction.employerslist.detail.GetEmployerDetailUseCase
import tech.hombre.domain.interaction.threadslist.messages.CreateThreadUseCase
import tech.hombre.domain.model.Countries
import tech.hombre.domain.model.EmployerDetail
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success
import tech.hombre.freelancehunt.ui.base.ViewState

class EmployerDetailViewModel(
    private val getEmployerDetail: GetEmployerDetailUseCase,
    private val countriesDao: CountriesDao,
    private val createThread: CreateThreadUseCase
) :
    BaseViewModel<EmployerDetail>() {

    val _countries = MutableLiveData<List<Countries.Data>>()
    val countries: LiveData<List<Countries.Data>>
        get() = _countries

    val _action = MutableLiveData<ViewState<String>>()
    val action: LiveData<ViewState<String>>
        get() = _action

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

    fun sendMessage(profileId: Int, subject: String, message: String) = executeUseCase {
        createThread(profileId, subject, message)
            .onSuccess {
                _action.value = Success("message")
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }
}

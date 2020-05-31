package tech.hombre.freelancehunt.ui.main.presentation

import tech.hombre.domain.interaction.countries.GetCountriesUseCase
import tech.hombre.domain.interaction.myprofile.GetMyProfileUseCase
import tech.hombre.domain.model.MyProfile
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success

class MainViewModel(
    private val getMyProfile: GetMyProfileUseCase,
    private val getCountries: GetCountriesUseCase
) :
    BaseViewModel<MyProfile.Data.Attributes>() {

    fun checkTokenByMyProfile(token: String = "") = executeUseCase {
        getMyProfile(token)
            .onSuccess { _viewState.value = Success(it.data.attributes) }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun refreshCountriesList() = executeUseCase {
        getCountries("")
            .onSuccess {}
            .onFailure { _viewState.value = Error(it.throwable) }
    }
}

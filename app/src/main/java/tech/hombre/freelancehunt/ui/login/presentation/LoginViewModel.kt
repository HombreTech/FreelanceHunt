package tech.hombre.freelancehunt.ui.login.presentation

import tech.hombre.data.local.LocalProperties
import tech.hombre.domain.interaction.myprofile.GetMyProfileUseCase
import tech.hombre.domain.model.MyProfile
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success

class LoginViewModel(private val getMyProfile: GetMyProfileUseCase, val appPreferences: LocalProperties) :
    BaseViewModel<MyProfile>() {

    fun checkTokenByMyProfile(token: String = "") = executeUseCase {
        appPreferences.setAccessToken(token)
        getMyProfile(token)
            .onSuccess { _viewState.value = Success(it) }
            .onFailure { _viewState.value = Error(it.throwable) }
    }
}

package tech.hombre.freelancehunt.ui.login.presentation

import tech.hombre.data.di.API_TOKEN
import tech.hombre.domain.interaction.myprofile.GetMyProfileUseCase
import tech.hombre.domain.model.MyProfile
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success

class LoginViewModel(private val getMyProfile: GetMyProfileUseCase) :
    BaseViewModel<MyProfile>() {

    fun checkTokenByMyProfile(token: String = "") = executeUseCase {
        API_TOKEN = token
        getMyProfile(token)
            .onSuccess { _viewState.value = Success(it) }
            .onFailure { _viewState.value = Error(it.throwable) }
    }
}

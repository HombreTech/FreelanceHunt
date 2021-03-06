package tech.hombre.freelancehunt.ui.login.view

import android.os.Bundle
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.domain.model.MyProfile
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.common.extensions.*
import tech.hombre.freelancehunt.routing.AppNavigator
import tech.hombre.freelancehunt.routing.ScreenType
import tech.hombre.freelancehunt.ui.base.*
import tech.hombre.freelancehunt.ui.login.presentation.LoginViewModel


class LoginActivity : BaseActivity() {

    override val isPublic = true

    private val viewModel: LoginViewModel by viewModel()

    override fun viewReady() {
        setTheme(R.style.WelcomeTheme)
        setContentView(R.layout.activity_login)
        if (!isLoggedUser()) {
            subscribeToData()
            token.onDone { signIn() }
            doLogin.onClick {
                signIn()
            }
        } else {
            val type = (intent.getSerializableExtra(AppNavigator.SCREEN_TYPE) ?: ScreenType.MAIN) as ScreenType
            val fromNotification = intent.getBooleanExtra(EXTRA_1, false)
            appNavigator.showMainActivity(type, fromNotification)
            finishAffinity()
        }

    }

    private fun signIn() {
        val token = token.text.toString()
        if (token.isNotEmpty()) {
            hideInputs()
            viewModel.checkTokenByMyProfile(token)
        } else showError(getString(R.string.token_is_empty))
    }

    private fun hideInputs() {
        token.gone()
        doLogin.gone()
    }

    private fun showInputs() {
        token.visible()
        doLogin.visible()
    }

    private fun subscribeToData() {
        viewModel.viewState.subscribe(this, ::handleViewState)
    }

    private fun handleViewState(viewState: ViewState<MyProfile>) {
        when (viewState) {
            is Loading -> showLoading(loginLoadingProgress)
            is Success -> saveProfileAndGo(viewState.data)
            is Error -> {
                handleError(viewState.error.localizedMessage)
                showInputs()
            }
            is NoInternetState -> {
                showNoInternetError()
                showInputs()
            }
        }
    }

    private fun handleError(error: String) {
        hideLoading(loginLoadingProgress)
        showError(error)
    }

    private fun showNoInternetError() {
        hideLoading(loginLoadingProgress)
        snackbar(getString(R.string.no_internet_error_message), cardView)
    }

    private fun saveProfileAndGo(myprofile: MyProfile) {
        hideLoading(loginLoadingProgress)
        appPreferences.setCurrentUserId(myprofile.data.id)
        appPreferences.setCurrentUserType(myprofile.data.type)
        appPreferences.setCurrentUserProfile(myprofile.data.attributes)
        appPreferences.setAccessToken(token.text.toString())
        appNavigator.showMainActivity()
        finishAffinity()
    }
}
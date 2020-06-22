package tech.hombre.freelancehunt.ui.base

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ProgressBar
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.drawer.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import tech.hombre.data.common.coroutine.CoroutineContextProvider
import tech.hombre.data.local.LocalProperties
import tech.hombre.freelancehunt.App
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.EMPTY_STRING
import tech.hombre.freelancehunt.common.UserType
import tech.hombre.freelancehunt.common.extensions.gone
import tech.hombre.freelancehunt.common.extensions.switch
import tech.hombre.freelancehunt.common.extensions.toast
import tech.hombre.freelancehunt.common.extensions.visible
import tech.hombre.freelancehunt.framework.billing.BillingClientModule
import tech.hombre.freelancehunt.routing.AppFragmentNavigator
import tech.hombre.freelancehunt.routing.AppNavigator
import tech.hombre.freelancehunt.ui.login.view.LoginActivity
import tech.hombre.freelancehunt.ui.main.view.activities.MainActivity
import tech.hombre.freelancehunt.ui.main.view.fragments.MainFragment

abstract class BaseActivity : AppCompatActivity() {

    protected val appNavigator: AppNavigator by inject { parametersOf(this) }

    protected val billingClient: BillingClientModule by inject()

    protected val appFragmentNavigator: AppFragmentNavigator by inject { parametersOf(this) }

    protected val appPreferences: LocalProperties by inject { parametersOf(this) }

    protected open val isPublic = false

    fun showError(errorMessage: String?) =
        toast(errorMessage ?: EMPTY_STRING)

    fun showLoading(progressBar: ProgressBar) = progressBar.visible()

    fun hideLoading(progressBar: ProgressBar) = progressBar.gone()

    private var backPressTimer: Long = 0

    private var _current_user_login: String? = null
    var current_user_login: String
        get() {
            if (_current_user_login == null) {
                _current_user_login = appPreferences.getCurrentUserProfile()?.login
            }
            return _current_user_login!!
        }
        set(value) {
            _current_user_login = value
        }

    fun getCurrentUser() = current_user_login

    override fun onBackPressed() {
        if (this is MainActivity) {
            if (supportFragmentManager.primaryNavigationFragment?.tag != MainFragment.TAG) {
                supportFragmentManager.switch(
                    R.id.fragmentContainer,
                    MainFragment.newInstance(),
                    MainFragment.TAG
                )
                selectMenuItem(R.id.menu_main, true)
            } else if (canExit()) {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

    private fun canExit(): Boolean {
        if (backPressTimer + 2000 > System.currentTimeMillis()) {
            return true
        } else {
            toast(getString(R.string.press_again_to_exit))
        }
        backPressTimer = System.currentTimeMillis()
        return false
    }

    override fun onStart() {
        overridePendingTransition(0, 0)
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isPublic && !validateAuth()) {
            return
        } else viewReady()
    }

    abstract fun viewReady()

    fun isLoggedUser() =
        appPreferences.getCurrentUserProfile() != null && appPreferences.getAccessToken()
            .isNotEmpty()

    open fun validateAuth(): Boolean {
        if (!isLoggedUser()) {
            onLoginRequire()
            return false
        }
        return true
    }

    fun onLoginRequire() {
        appPreferences.resetPreferences()
        val glide = Glide.get(App.instance)
        CoroutineScope(CoroutineContextProvider().io).launch {
            glide.clearDiskCache()
        }
        glide.clearMemory()
        appNavigator.showLoginActivity()
        finishAffinity()
    }

    fun onShowMyProfile() = when (UserType.EMPLOYER.type) {
        appPreferences.getCurrentUserType() -> appNavigator.showEmployerDetails(appPreferences.getCurrentUserId())
        else -> appNavigator.showFreelancerDetails(appPreferences.getCurrentUserId())
    }

    fun selectMenuItem(@IdRes id: Int, check: Boolean) {
        navigation?.let {
            it.menu.findItem(id)?.let { item ->
                with(item) {
                    isCheckable = check
                    isChecked = check
                }
            }
        }
    }

    private fun getActivity(content: Context?): Activity? {
        return when (content) {
            null -> null
            is Activity -> content
            is ContextWrapper -> getActivity(content.baseContext)
            else -> null
        }
    }

    fun shareUrl(context: Context, url: String) {
        val activity = getActivity(context)
            ?: throw IllegalArgumentException("Context given is not an instance of activity ${context.javaClass.name}")
        try {
            ShareCompat.IntentBuilder.from(activity)
                .setChooserTitle(context.getString(R.string.share))
                .setType("text/plain")
                .setText(url)
                .startChooser()
        } catch (e: ActivityNotFoundException) {
            showError(e.message!!)
        }
    }

    fun openUrl(context: Context, url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(Intent.createChooser(browserIntent, context.getString(R.string.open)))
    }

}
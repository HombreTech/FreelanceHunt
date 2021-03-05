package tech.hombre.freelancehunt.ui.main.view.activities

import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.text.SpannableString
import android.text.util.Linkify
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.work.*
import kotlinx.android.synthetic.main.activity_container.*
import kotlinx.android.synthetic.main.appbar.*
import kotlinx.android.synthetic.main.drawer.*
import kotlinx.android.synthetic.main.item_menu_threads.view.*
import kotlinx.android.synthetic.main.navigation_header.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.domain.model.MyProfile
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.common.SKU_PREMIUM
import tech.hombre.freelancehunt.common.UserType
import tech.hombre.freelancehunt.common.extensions.subscribe
import tech.hombre.freelancehunt.common.extensions.switch
import tech.hombre.freelancehunt.common.provider.AutoStartPermissionHelper
import tech.hombre.freelancehunt.common.provider.PowerSaverHelper
import tech.hombre.freelancehunt.common.widgets.BadgeDrawerArrowDrawable
import tech.hombre.freelancehunt.framework.app.AppHelper
import tech.hombre.freelancehunt.framework.tasks.FeedWorker
import tech.hombre.freelancehunt.framework.tasks.ProjectsWorker
import tech.hombre.freelancehunt.framework.tasks.ThreadsWorker
import tech.hombre.freelancehunt.routing.AppNavigator
import tech.hombre.freelancehunt.routing.ScreenType
import tech.hombre.freelancehunt.ui.base.BaseActivity
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success
import tech.hombre.freelancehunt.ui.base.ViewState
import tech.hombre.freelancehunt.ui.employers.view.EmployersFragment
import tech.hombre.freelancehunt.ui.freelancers.view.FreelancersFragment
import tech.hombre.freelancehunt.ui.main.presentation.MainPublicViewModel
import tech.hombre.freelancehunt.ui.main.presentation.MainViewModel
import tech.hombre.freelancehunt.ui.main.view.fragments.AboutFragment
import tech.hombre.freelancehunt.ui.main.view.fragments.MainFragment
import tech.hombre.freelancehunt.ui.main.view.fragments.SettingFragment
import tech.hombre.freelancehunt.ui.my.bids.view.MyBidsFragment
import tech.hombre.freelancehunt.ui.my.contests.view.MyContestsFragment
import tech.hombre.freelancehunt.ui.my.projects.view.MyProjectsFragment
import tech.hombre.freelancehunt.ui.my.workspaces.view.MyWorkspacesFragment
import tech.hombre.freelancehunt.ui.threads.view.ThreadsFragment
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : BaseActivity() {

    private lateinit var drawerToggle: ActionBarDrawerToggle

    private lateinit var badgeToggleDrawable: BadgeDrawerArrowDrawable

    private val viewModel: MainViewModel by viewModel()

    private val sharedViewModelMain: MainPublicViewModel by viewModel()

    private var timer = Timer()

    // TODO to preferences
    private val delay = 15000L

    override fun viewReady() {
        setContentView(R.layout.activity_container)
        initViews()
        subscribeToData()
        when (intent.getSerializableExtra(AppNavigator.SCREEN_TYPE)) {
            ScreenType.MAIN -> {
                supportFragmentManager.switch(
                    R.id.fragmentContainer,
                    MainFragment.newInstance(),
                    MainFragment.TAG
                )
                selectMenuItem(R.id.menu_main, true)
            }
            ScreenType.FEED ->
                supportFragmentManager.switch(
                    R.id.fragmentContainer,
                    MainFragment.newInstance(true),
                    MainFragment.TAG
                )
            ScreenType.THREADS -> supportFragmentManager.switch(
                R.id.fragmentContainer,
                ThreadsFragment.newInstance(),
                ThreadsFragment.TAG
            )
            else -> finish()
        }
        if (!intent.getBooleanExtra(EXTRA_1, false))
            CoroutineScope(Dispatchers.Default).launch {
                setupTasks()
            }
        billingClient.init()
        checkPermissions()
    }

    private fun checkPermissions() {
        val intent = PowerSaverHelper.prepareIntentForWhiteListingOfBatteryOptimization(
            this,
            onSuccess = showDeveloperNotify()
        )
        intent.intent?.let {
            startActivity(it)
        } ?: if (!intent.isWhiteListed) if (!appPreferences.isAutoStartPermissionsRequired()) {
            if (AutoStartPermissionHelper.getInstance().isAutoStartPermissionAvailable(this)) with(
                AlertDialog.Builder(
                    this,
                    AppHelper.getDialogTheme(appPreferences.getAppTheme())
                )
            ) {
                setCancelable(false)
                setMessage(getString(R.string.autostart_permissions_dialog_info))
                setPositiveButton(android.R.string.ok) { dialog: DialogInterface, _: Int ->

                    if (AutoStartPermissionHelper.getInstance().getAutoStartPermission(context)) {
                        appPreferences.setAutoStartPermissionsRequired()
                        showDeveloperNotify()
                    } else with(
                        AlertDialog.Builder(
                            context,
                            AppHelper.getDialogTheme(appPreferences.getAppTheme())
                        )
                    ) {
                        setCancelable(false)
                        setMessage(getString(R.string.autostart_permissions_required_error))
                        setPositiveButton(android.R.string.ok) { dialog: DialogInterface, _: Int ->
                            appPreferences.setAutoStartPermissionsRequired()
                            showDeveloperNotify()
                        }
                        show()
                    }
                }
                show()
            } else {
                appPreferences.setAutoStartPermissionsRequired()
                showDeveloperNotify()
            }
        }
    }

    private fun showDeveloperNotify() {
        if (appPreferences.isDeveloperNotifyShowed()) return
        val message = SpannableString(getString(R.string.developer_notify))
        Linkify.addLinks(message, Linkify.ALL)
        with(
            AlertDialog.Builder(
                this,
                AppHelper.getDialogTheme(appPreferences.getAppTheme())
            )
        )
        {
            setCancelable(false)
            setMessage(message)
            setPositiveButton(android.R.string.ok) { dialog: DialogInterface, _: Int ->
                appPreferences.setDeveloperNotifyShowed()
            }
            show()
        }
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers()
        } else super.onBackPressed()
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        drawerToggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerToggle.syncState()

        drawer.addDrawerListener(drawerToggle)
        navigation.menu.findItem(R.id.menu_contests).isVisible =
            appPreferences.getCurrentUserType() == UserType.EMPLOYER.type
        navigation.menu.findItem(R.id.menu_bids).isVisible =
            appPreferences.getCurrentUserType() == UserType.FREELANCER.type
        navigation.menu.findItem(R.id.menu_projects).isVisible =
            appPreferences.getCurrentUserType() == UserType.EMPLOYER.type
        navigation.setNavigationItemSelectedListener {
            if (navigation.checkedItem != it) {
                when (it.itemId) {
                    R.id.menu_main -> supportFragmentManager.switch(
                        R.id.fragmentContainer,
                        MainFragment.newInstance(),
                        MainFragment.TAG
                    )
                    R.id.menu_profile -> onShowMyProfile()
                    R.id.menu_logout -> onLoginRequire()
                    R.id.menu_about -> supportFragmentManager.switch(
                        R.id.fragmentContainer,
                        AboutFragment.newInstance(),
                        AboutFragment.TAG
                    )
                    R.id.menu_contests -> supportFragmentManager.switch(
                        R.id.fragmentContainer,
                        MyContestsFragment(),
                        MyContestsFragment.TAG
                    )
                    R.id.menu_settings -> supportFragmentManager.switch(
                        R.id.fragmentContainer,
                        SettingFragment(),
                        SettingFragment.TAG
                    )
                    R.id.menu_freelancers -> supportFragmentManager.switch(
                        R.id.fragmentContainer,
                        FreelancersFragment.newInstance(),
                        FreelancersFragment.TAG
                    )
                    R.id.menu_employers -> supportFragmentManager.switch(
                        R.id.fragmentContainer,
                        EmployersFragment.newInstance(),
                        EmployersFragment.TAG
                    )
                    R.id.menu_threads -> supportFragmentManager.switch(
                        R.id.fragmentContainer,
                        ThreadsFragment.newInstance(),
                        ThreadsFragment.TAG
                    )
                    R.id.menu_bids -> supportFragmentManager.switch(
                        R.id.fragmentContainer,
                        MyBidsFragment.newInstance(),
                        MyBidsFragment.TAG
                    )
                    R.id.menu_projects -> supportFragmentManager.switch(
                        R.id.fragmentContainer,
                        MyProjectsFragment.newInstance(),
                        MyProjectsFragment.TAG
                    )
                    R.id.menu_workspaces -> supportFragmentManager.switch(
                        R.id.fragmentContainer,
                        MyWorkspacesFragment.newInstance(),
                        MyWorkspacesFragment.TAG
                    )
                    R.id.menu_buy_premium -> billingClient.launchBilling(this, SKU_PREMIUM)
                }
                if (it.itemId != R.id.menu_profile && it.itemId != R.id.menu_buy_premium) {
                    selectMenuItem(it.itemId, true)
                }
            }
            drawer.closeDrawers()
            true
        }
        updateHeader(appPreferences.getCurrentUserProfile()!!)
    }

    private fun subscribeToData() {
        viewModel.viewState.subscribe(this, ::handleViewState)
        viewModel.hasMessages.subscribe(this, ::handleMessagesState)
        viewModel.feedEvents.subscribe(this, ::handleFeedState)
        sharedViewModelMain.messagesCounter.subscribe(this) {
            updateDrawer(it, null)
        }
        billingClient.isPremium.subscribe(this, ::handlePremiumState)
        viewModel.checkTokenByMyProfile(appPreferences.getAccessToken())
        viewModel.checkFeed()
        viewModel.refreshCountriesList()
        viewModel.refreshSkillsList()
        timer.schedule(timerTask, delay, delay)
    }

    private fun handlePremiumState(isPremium: Boolean) {
        val header = navigation.getHeaderView(0)
        header.isPlus.isVisible = isPremium
        navigation.menu.findItem(R.id.menu_buy_premium).isVisible = !isPremium
    }

    private fun handleViewState(viewState: ViewState<MyProfile.Data.Attributes>) {
        when (viewState) {
            is Success -> {
                updateHeader(viewState.data)
                updateDrawer(null, viewState.data.rating)
                appPreferences.setCurrentUserProfile(viewState.data)
                viewModel.checkMessages()
            }
            is Error -> handleError(viewState.error.localizedMessage)
        }
    }

    private fun updateDrawer(newMassages: Boolean?, rating: Int?) {
        if (rating != null) navigation.menu.findItem(R.id.menu_profile).actionView?.let {
            it.subtitle.text = rating.toString()
        }
        if (newMassages != null) navigation.menu.findItem(R.id.menu_threads).actionView?.let {
            if (newMassages) {
                it.icon.setImageResource(R.drawable.mail)
                it.subtitle.text = getString(R.string.have_messages)
                badgeToggleDrawable = BadgeDrawerArrowDrawable(supportActionBar?.themedContext)
                drawerToggle.drawerArrowDrawable = badgeToggleDrawable

            } else {
                it.icon.setImageResource(R.drawable.mail_empty)
                it.subtitle.text = getString(R.string.not_have_messages)
                badgeToggleDrawable = BadgeDrawerArrowDrawable(supportActionBar?.themedContext)
                drawerToggle.drawerArrowDrawable =
                    DrawerArrowDrawable(supportActionBar?.themedContext)
            }
        }

    }

    private fun handleMessagesState(messageViewState: ViewState<Boolean>) {
        when (messageViewState) {
            is Success -> {
                updateDrawer(messageViewState.data, null)
            }
            is Error -> handleError(messageViewState.error.localizedMessage)
        }
    }

    private fun handleFeedState(feedViewState: ViewState<Int>) {
        when (feedViewState) {
            is Success -> {
                sharedViewModelMain.setFeedBadgeCounter(feedViewState.data)
            }
            is Error -> handleError(feedViewState.error.localizedMessage)
        }
    }

    private fun handleError(error: String) {
        showError(error)
    }

    private fun updateHeader(profile: MyProfile.Data.Attributes) {
        val header = navigation.getHeaderView(0)
        header.apply {
            avatar.setUrl(profile.avatar.large.url, isCircle = true)
            avatar.setOnClickListener { onShowMyProfile() }
            isPlus.isVisible = profile.is_plus_active
            name.text = "${profile.first_name} ${profile.last_name}"
            userType.text =
                if (appPreferences.getCurrentUserType() == UserType.EMPLOYER.type) getString(R.string.employer) else getString(
                    R.string.freelancer
                )
        }

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    private fun setupTasks() {

        val networkType =
            if (appPreferences.getWorkerUnmeteredEnabled()) NetworkType.UNMETERED else NetworkType.CONNECTED

        val constrains = Constraints.Builder()
            .setRequiredNetworkType(networkType)
            .build()

        if (appPreferences.getWorkerFeedEnabled())
            WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                FeedWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                PeriodicWorkRequestBuilder<FeedWorker>(
                    appPreferences.getWorkerInterval(), TimeUnit.MINUTES
                ).setConstraints(constrains).build()
            )
        if (appPreferences.getWorkerMessagesEnabled())
            WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                ThreadsWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                PeriodicWorkRequestBuilder<ThreadsWorker>(
                    appPreferences.getWorkerInterval(), TimeUnit.MINUTES
                ).setConstraints(constrains).build()
            )
        if (appPreferences.getWorkerProjectsEnabled())
            WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                ProjectsWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                PeriodicWorkRequestBuilder<ProjectsWorker>(
                    appPreferences.getWorkerInterval(), TimeUnit.MINUTES
                ).setConstraints(constrains).build()
            )
    }

    private val timerTask = object : TimerTask() {
        override fun run() {
            runOnUiThread {
                if (navigation.checkedItem != navigation.menu.findItem(R.id.menu_threads)) viewModel.checkMessages()
            }
        }
    }

    override fun finish() {
        timer.cancel()
        timer.purge()
        super.finish()
    }

}

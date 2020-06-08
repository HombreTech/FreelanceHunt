package tech.hombre.freelancehunt.ui.main.view.activities

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
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
import tech.hombre.freelancehunt.common.UserType
import tech.hombre.freelancehunt.common.extensions.subscribe
import tech.hombre.freelancehunt.common.extensions.switch
import tech.hombre.freelancehunt.framework.tasks.FeedWorker
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
import tech.hombre.freelancehunt.ui.main.view.fragments.MainFragment
import tech.hombre.freelancehunt.ui.main.view.fragments.SettingFragment
import tech.hombre.freelancehunt.ui.my.bids.view.MyBidsFragment
import tech.hombre.freelancehunt.ui.threads.view.ThreadsFragment
import java.util.concurrent.TimeUnit


class MainActivity : BaseActivity() {

    override fun isPrivate() = true

    private lateinit var drawerToggle: ActionBarDrawerToggle

    private val viewModel: MainViewModel by viewModel()

    private val sharedViewModelMain: MainPublicViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> true
        }
        return super.onOptionsItemSelected(item)
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
                }
                selectMenuItem(it.itemId, true)
            }
            drawer.closeDrawers()
            true
        }
        updateHeader(appPreferences.getCurrentUserProfile()!!)
    }

    private fun subscribeToData() {
        viewModel.viewState.subscribe(this, ::handleViewState)
        viewModel.hasMessages.subscribe(this, ::handleMessagesState)
        sharedViewModelMain.messagesCounter.subscribe(this) {
            updateDrawer(it, null)
        }
        viewModel.checkTokenByMyProfile(appPreferences.getAccessToken())
        viewModel.refreshCountriesList()
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
                it.subtitle.text = getString(R.string.have_messages)
            } else it.subtitle.text = getString(R.string.not_have_messages)
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

    private fun handleError(error: String) {
        showError(error)
    }

    private fun updateHeader(profile: MyProfile.Data.Attributes) {
        val header = navigation.getHeaderView(0)
        header.avatar.setUrl(profile.avatar.large.url, isCircle = true)
        header.avatar.setOnClickListener { onShowMyProfile() }
        header.name.text = "${profile.first_name} ${profile.last_name}"
        header.userType.text =
            if (appPreferences.getCurrentUserType() == UserType.EMPLOYER.type) getString(R.string.employer) else getString(
                R.string.freelancer
            )
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

        val networkType = if (appPreferences.getWorkerUnmeteredEnabled()) NetworkType.UNMETERED else NetworkType.CONNECTED

        val constrains = Constraints.Builder()
            .setRequiredNetworkType(networkType)
            .build()

        if (appPreferences.getWorkerFeedEnabled())
            WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                FeedWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                PeriodicWorkRequestBuilder<FeedWorker>(
                    appPreferences.getWorkerInterval(), TimeUnit.MINUTES
                ).setConstraints(constrains).addTag(FeedWorker.WORK_NAME).build()
            )
        if (appPreferences.getWorkerMessagesEnabled())
            WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                ThreadsWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                PeriodicWorkRequestBuilder<ThreadsWorker>(
                    appPreferences.getWorkerInterval(), TimeUnit.MINUTES
                ).setConstraints(constrains).addTag(ThreadsWorker.WORK_NAME).build()
            )
    }

}

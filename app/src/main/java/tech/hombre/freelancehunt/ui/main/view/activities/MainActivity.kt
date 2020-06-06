package tech.hombre.freelancehunt.ui.main.view.activities

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.activity_container.*
import kotlinx.android.synthetic.main.appbar.*
import kotlinx.android.synthetic.main.drawer.*
import kotlinx.android.synthetic.main.item_menu_threads.view.*
import kotlinx.android.synthetic.main.navigation_header.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.domain.model.MyProfile
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.UserType
import tech.hombre.freelancehunt.common.extensions.*
import tech.hombre.freelancehunt.routing.AppNavigator
import tech.hombre.freelancehunt.routing.ScreenType
import tech.hombre.freelancehunt.ui.base.BaseActivity
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success
import tech.hombre.freelancehunt.ui.base.ViewState
import tech.hombre.freelancehunt.ui.employers.view.EmployersFragment
import tech.hombre.freelancehunt.ui.freelancers.view.FreelancersFragment
import tech.hombre.freelancehunt.ui.main.presentation.MainViewModel
import tech.hombre.freelancehunt.ui.main.view.fragments.MainFragment
import tech.hombre.freelancehunt.ui.my.bids.view.MyBidsFragment
import tech.hombre.freelancehunt.ui.threads.view.ThreadsFragment


class MainActivity : BaseActivity() {

    override fun isPrivate() = true

    private lateinit var drawerToggle: ActionBarDrawerToggle
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        initViews()
        subscribeToData()
        when (intent.getSerializableExtra(AppNavigator.SCREEN_TYPE)) {
            ScreenType.MAIN -> addFragment(MainFragment.newInstance(), R.id.fragmentContainer)
            else -> finish()
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
                    R.id.menu_profile -> onShowMyProfile()
                    R.id.menu_logout -> onLoginRequire()
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
                /*Handler().postDelayed({
                    when (it.itemId) {
                        R.id.menu_profile -> appNavigator.showProfileActivity()
                        R.id.menu_freelancers -> supportFragmentManager.displayFragment(R.id.fragmentContainer, FreelancersFragment.newInstance(), FreelancersFragment.TAG)
                        R.id.menu_employers -> supportFragmentManager.displayFragment(R.id.fragmentContainer, EmployersFragment.newInstance(), EmployersFragment.TAG)
                        R.id.menu_threads -> supportFragmentManager.displayFragment(R.id.fragmentContainer, ThreadsFragment.newInstance(), ThreadsFragment.TAG)
                        R.id.menu_bids -> supportFragmentManager.displayFragment(R.id.fragmentContainer, MyBidsFragment.newInstance(), MyBidsFragment.TAG)
                    }
                    selectMenuItem(it.itemId, true)
                }, 256)*/
            }
            drawer.closeDrawers()
            true
        }
        updateHeader(appPreferences.getCurrentUserProfile()!!)
    }

    private fun subscribeToData() {
        viewModel.viewState.subscribe(this, ::handleViewState)
        viewModel.hasMessages.subscribe(this, ::handleMessagesState)
        viewModel.checkTokenByMyProfile(appPreferences.getAccessToken())
        viewModel.refreshCountriesList()
    }

    private fun handleViewState(viewState: ViewState<MyProfile.Data.Attributes>) {
        when (viewState) {
            is Success -> {
                updateHeader(viewState.data)
                appPreferences.setCurrentUserProfile(viewState.data)
                viewModel.checkMessages()
            }
            is Error -> handleError(viewState.error.localizedMessage)
        }
    }

    private fun handleMessagesState(messageViewState: ViewState<Boolean>) {
        when (messageViewState) {
            is Success -> {
                val view = navigation.menu.findItem(R.id.menu_threads).actionView
                if (messageViewState.data) {
                    view.subtitle.text = getString(R.string.have_messages)
                } else view.subtitle.text = getString(R.string.not_have_messages)
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

    private fun selectMenuItem(@IdRes id: Int, check: Boolean) {
        navigation?.let {
            it.menu.findItem(id)?.let { item ->
                with(item) {
                    isCheckable = check
                    isChecked = check
                }
            }
        }
    }

}

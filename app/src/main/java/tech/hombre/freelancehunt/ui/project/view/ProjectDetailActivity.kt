package tech.hombre.freelancehunt.ui.project.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_project_detail.*
import kotlinx.android.synthetic.main.appbar_fill.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.domain.model.Countries
import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.ProjectDetail
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.*
import tech.hombre.freelancehunt.common.extensions.*
import tech.hombre.freelancehunt.ui.base.*
import tech.hombre.freelancehunt.ui.menu.AddBidBottomDialogFragment
import tech.hombre.freelancehunt.ui.menu.BottomMenuBuilder
import tech.hombre.freelancehunt.ui.project.presentation.ProjectDetailViewModel
import tech.hombre.freelancehunt.ui.project.presentation.ProjectPublicViewModel
import tech.hombre.freelancehunt.ui.project.view.pager.PagerProjectBids
import tech.hombre.freelancehunt.ui.project.view.pager.PagerProjectComments
import tech.hombre.freelancehunt.ui.project.view.pager.PagerProjectOverview

class ProjectDetailActivity : BaseActivity(), AddBidBottomDialogFragment.OnBidAddedListener {

    override fun isPrivate() = false

    private val viewModel: ProjectDetailViewModel by viewModel()

    private val projectPublicViewModel: ProjectPublicViewModel by viewModel()

    var countries = listOf<Countries.Data>()

    var projectUrl = ""

    private lateinit var pagerAdapter: PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (savedInstanceState == null) {
            intent?.extras?.let {
                subscribeToData()
                val locally = it.getBoolean(EXTRA_2, false)
                if (!locally) {
                    val project: ProjectDetail.Data? = it.getParcelable(EXTRA_1)
                    initProjectDetails(project!!)
                } else {
                    viewModel.getProjectDetails("projects/${it.getInt(EXTRA_1)}")
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_project, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> shareUrl(this, projectUrl)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun subscribeToData() {
        viewModel.viewState.subscribe(this, ::handleViewState)
        viewModel.countries.subscribe(this, {
            countries = it
        })
        projectPublicViewModel.badgeCounter.subscribe(this) { badges ->
            tabs.getTabAt(badges.first)?.let {
                val badge: BadgeDrawable = it.orCreateBadge
                badge.isVisible = true
                badge.number = badges.second
            }
        }
        projectPublicViewModel.tabViewsRefresher.subscribe(this) { tab ->
            updateTabViews(tab)
        }
        viewModel.setCountries()
    }

    private fun handleViewState(viewState: ViewState<ProjectDetail>) {
        when (viewState) {
            is Loading -> showLoading(progressBar)
            is Success -> initProjectDetails(viewState.data.data)
            is Error -> {
                handleError(viewState.error.localizedMessage)
                finish()
            }
            is NoInternetState -> showNoInternetError()
        }
    }

    private fun initProjectDetails(details: ProjectDetail.Data) {
        hideLoading(progressBar)

        projectUrl = details.links.self.web

        toolbar.subtitle = details.attributes.name

        pagerAdapter = PagerAdapter(this, details)
        with(pager) {
            adapter = pagerAdapter
            offscreenPageLimit = 3
            isUserInputEnabled = false
        }
        val tabsTitles = resources.getStringArray(R.array.project_tabs)
        val tabsIcons = resources.getStringArray(R.array.project_tabs_icons)

        TabLayoutMediator(tabs, pager, false, false) { tab, position ->
            tab.text = tabsTitles[position]
            tab.icon = ContextCompat.getDrawable(this, getDrawableIdByName(tabsIcons[position]))
            pager.setCurrentItem(tab.position, false)
        }.attach()
        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateTabViews(position)
                hideShowFab(position)
            }

            private fun hideShowFab(position: Int) {
                when (position) {
                    0 -> fab.hide()
                    1 -> {
                        if (details.attributes.is_only_for_plus && appPreferences.getCurrentUserProfile()?.is_plus_active != true) {
                            fab.hide()
                            return
                        }
                        val status = ProjectStatus.values().find { it.id == details.attributes.status.id }
                        if (appPreferences.getCurrentUserType() == UserType.FREELANCER.type && status == ProjectStatus.OPEN_FOR_PROPOSALS) {
                            fab.setImageResource(R.drawable.bid)
                            fab.show()

                        } else fab.hide()
                    }
                    2 -> fab.hide()
                }
            }
        })

        isplus.visibility = details.attributes.is_only_for_plus.toVisibleState()
        premium.visibility = details.attributes.is_premium.toVisibleState()

        safe.text = getTitleBySafeType(
            this,
            SafeType.values().find { it.type == details.attributes.safe_type }
                ?: SafeType.DIRECT_PAYMENT)

        isremote.visibility = details.attributes.is_remote_job.toVisibleState()
        name.text = details.attributes.name
        status.text = details.attributes.status.name

        if (details.attributes.budget != null) {
            budget.text =
                "${details.attributes.budget!!.amount} ${currencyToChar(details.attributes.budget!!.currency)}"
        } else budget.text = getString(R.string.budget_nan)

        expiredAt.text = details.attributes.expired_at.parseFullDate(true).getTimeAgo()

        fab.setOnClickListener {
            when (tabs.selectedTabPosition) {
                1 -> {
                    if (details.attributes.is_only_for_plus && appPreferences.getCurrentUserProfile()?.is_plus_active == true)
                        BottomMenuBuilder(
                            supportFragmentManager,
                            AddBidBottomDialogFragment.TAG
                        ).buildMenuForAddBid(
                            appPreferences.getCurrentUserProfile()?.is_plus_active ?: false,
                            details.id
                        )
                    else if (!details.attributes.is_only_for_plus) {
                        BottomMenuBuilder(
                            supportFragmentManager,
                            AddBidBottomDialogFragment.TAG
                        ).buildMenuForAddBid(
                            appPreferences.getCurrentUserProfile()?.is_plus_active ?: false,
                            details.id
                        )
                    } else {
                        handleError(getString(R.string.only_for_plus))
                    }
                }
            }
        }
    }

    private fun updateTabViews(position: Int) {
        val view = pagerAdapter.getViewAtPosition(position)
        view?.let {
            updatePagerHeightForChild(view, pager)
        }
    }

    private fun updatePagerHeightForChild(view: View, pager: ViewPager2) {
        view.post {
            val wMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
            val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            view.measure(wMeasureSpec, hMeasureSpec)

            if (pager.layoutParams.height != view.measuredHeight) {
                pager.layoutParams = (pager.layoutParams)
                    .also { lp ->
                        lp.height = view.measuredHeight
                    }
            }
        }
    }

    private fun handleError(error: String) {
        hideLoading(progressBar)
        showError(error)
    }


    private fun showNoInternetError() {
        hideLoading(progressBar)
        snackbar(getString(R.string.no_internet_error_message), projectActivityContainer)
    }

    private inner class PagerAdapter(
        fa: FragmentActivity,
        details: ProjectDetail.Data
    ) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 3

        val fragments = arrayListOf<Fragment>(
            PagerProjectOverview.newInstance(details.attributes),
            PagerProjectBids.newInstance(details.id, details.attributes.employer!!.id),
            PagerProjectComments.newInstance(details.id)
        )

        fun getViewAtPosition(position: Int) = fragments[position].view

        override fun createFragment(position: Int): Fragment = when (position) {
            0 -> fragments[0]
            1 -> fragments[1]
            2 -> fragments[2]
            else -> fragments[0]
        }
    }

    override fun onBidAdded(
        id: Int,
        days: Int,
        budget: MyBidsList.Data.Attributes.Budget,
        safeType: SafeType,
        comment: String,
        isHidden: Boolean
    ) {
        (pagerAdapter.fragments[1] as PagerProjectBids).onBidAdded(
            id,
            days,
            budget,
            safeType,
            comment,
            isHidden
        )
    }


}
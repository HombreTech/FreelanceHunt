package tech.hombre.freelancehunt.ui.contest.view

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
import kotlinx.android.synthetic.main.activity_contest_detail.*
import kotlinx.android.synthetic.main.appbar_fill.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.domain.model.ContestDetail
import tech.hombre.domain.model.Countries
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.common.EXTRA_2
import tech.hombre.freelancehunt.common.extensions.*
import tech.hombre.freelancehunt.ui.base.*
import tech.hombre.freelancehunt.ui.contest.presentation.ContestDetailViewModel
import tech.hombre.freelancehunt.ui.contest.presentation.ContestPublicViewModel
import tech.hombre.freelancehunt.ui.contest.view.pager.PagerContestComments
import tech.hombre.freelancehunt.ui.contest.view.pager.PagerContestOverview


class ContestDetailActivity : BaseActivity() {

    override fun isPrivate() = false

    private val viewModel: ContestDetailViewModel by viewModel()

    private val contestPublicViewModel: ContestPublicViewModel by viewModel()

    var contestId = 0

    var contestUrl = ""

    var countries = listOf<Countries.Data>()

    private lateinit var pagerAdapter: PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contest_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (savedInstanceState == null) {
            contestId = intent?.extras?.getInt(EXTRA_1, -1) ?: -1
        }
        if (savedInstanceState == null) {
            intent?.extras?.let {
                subscribeToData()
                val locally = it.getBoolean(EXTRA_2, false)
                if (!locally) {
                    val contest: ContestDetail.Data? = it.getParcelable(EXTRA_1)
                    initContestDetails(contest!!)
                } else {
                    viewModel.getContestDetails(it.getInt(EXTRA_1))
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_contest, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> shareUrl(this, contestUrl)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun subscribeToData() {
        viewModel.viewState.subscribe(this, ::handleViewState)
        viewModel.countries.subscribe(this, {
            countries = it
        })
        contestPublicViewModel.badgeCounter.subscribe(this) { badges ->
            tabs.getTabAt(badges.first)?.let {
                val badge: BadgeDrawable = it.orCreateBadge
                badge.isVisible = true
                badge.number = badges.second
            }
        }
        viewModel.setCountries()
    }

    private fun handleViewState(viewState: ViewState<ContestDetail>) {
        when (viewState) {
            is Loading -> showLoading(progressBar)
            is Success -> initContestDetails(viewState.data.data)
            is Error -> handleError(viewState.error.localizedMessage)
            is NoInternetState -> showNoInternetError()
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

    private fun initContestDetails(details: ContestDetail.Data) {
        hideLoading(progressBar)

        contestUrl = details.links.self.web

        toolbar.subtitle = details.attributes.name

        pagerAdapter = PagerAdapter(this, details)
        with(pager) {
            adapter = pagerAdapter
            offscreenPageLimit = 2
            isUserInputEnabled = false
        }
        val tabsTitles = resources.getStringArray(R.array.contest_tabs)
        val tabsIcons = resources.getStringArray(R.array.contest_tabs_icons)

        TabLayoutMediator(tabs, pager, false, false) { tab, position ->
            tab.text = tabsTitles[position]
            tab.icon = ContextCompat.getDrawable(this, getDrawableIdByName(tabsIcons[position]))
            pager.setCurrentItem(tab.position, false)
        }.attach()
        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateTabViews(position)
            }
        })

        skillIcon.setUrl(
            "https://freelancehunt.com/static/images/contest/${getIconIdBySkillId(
                details.attributes.skill.id
            )}.png"
        )
        skill.text = details.attributes.skill.name
        name.text = details.attributes.name
        status.text = details.attributes.status.name
        budget.text =
            "${details.attributes.budget.amount} ${currencyToChar(details.attributes.budget!!.currency)}"

        finalAt.text = details.attributes.final_started_at.parseFullDate(true).getTimeAgo()
        applications.text =
            details.attributes.application_count.getEnding(this, R.array.ending_applications)
    }

    private fun handleError(error: String) {
        hideLoading(progressBar)
        showError(error)
    }

    private fun showNoInternetError() {
        hideLoading(progressBar)
        snackbar(getString(R.string.no_internet_error_message), contestActivityContainer)
    }


    private inner class PagerAdapter(
        fa: FragmentActivity,
        details: ContestDetail.Data
    ) : FragmentStateAdapter(fa) {

        override fun getItemCount(): Int = 2

        val fragments = arrayListOf<Fragment>(
            PagerContestOverview.newInstance(details.attributes),
            PagerContestComments.newInstance(details.id)
        )

        fun getViewAtPosition(position: Int) = fragments[position].view

        override fun createFragment(position: Int): Fragment = when (position) {
            0 -> fragments[0]
            1 -> fragments[1]
            else -> fragments[0]
        }
    }

}
package tech.hombre.freelancehunt.ui.freelancers.view

import android.graphics.PorterDuff
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
import kotlinx.android.synthetic.main.activity_freelancer_detail.*
import kotlinx.android.synthetic.main.appbar_fill.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.domain.model.FreelancerDetail
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.common.EXTRA_2
import tech.hombre.freelancehunt.common.extensions.*
import tech.hombre.freelancehunt.ui.base.*
import tech.hombre.freelancehunt.ui.freelancers.presentation.FreelancerDetailViewModel
import tech.hombre.freelancehunt.ui.freelancers.presentation.FreelancerPublicViewModel
import tech.hombre.freelancehunt.ui.freelancers.view.pager.PagerFreelancerOverview
import tech.hombre.freelancehunt.ui.freelancers.view.pager.PagerFreelancerReviews


class FreelancerDetailActivity : BaseActivity() {

    override fun isPrivate() = false

    private val viewModel: FreelancerDetailViewModel by viewModel()

    private val freelancerPublicViewModel: FreelancerPublicViewModel by viewModel()

    var countryId = -1

    private lateinit var pagerAdapter: PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freelancer_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (savedInstanceState == null) {
            intent?.extras?.let {
                subscribeToData()
                val locally = it.getBoolean(EXTRA_2, false)
                if (!locally) {
                    val profile: FreelancerDetail.Data? = it.getParcelable(EXTRA_1)
                    initFreelancerDetails(profile!!)
                } else {
                    viewModel.getFreelancerDetails(it.getInt(EXTRA_1))
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_freelancer, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun subscribeToData() {
        viewModel.viewState.subscribe(this, ::handleViewState)
        viewModel.countries.subscribe(this, {
            if (countryId != -1) {
                val country = it.find { it.id == countryId }
                if (country != null) locationIcon.setUrlSVG("https://freelancehunt.com/static/images/flags/4x3/${country.iso2.toLowerCase()}.svg")
            }
        })
        freelancerPublicViewModel.badgeCounter.subscribe(this) { badges ->
            tabs.getTabAt(badges.first)?.let {
                val badge: BadgeDrawable = it.orCreateBadge
                badge.isVisible = true
                badge.number = badges.second
            }
        }
        viewModel.setCountries()
    }

    private fun handleViewState(viewState: ViewState<FreelancerDetail>) {
        when (viewState) {
            is Loading -> showLoading(progressBar)
            is Success -> {
                initFreelancerDetails(viewState.data.data)
                viewModel.setCountries()
            }
            is Error -> handleError(viewState.error.localizedMessage)
            is NoInternetState -> showNoInternetError()
        }
    }

    private fun initFreelancerDetails(details: FreelancerDetail.Data) {
        hideLoading(progressBar)

        toolbar.subtitle = details.attributes.login

        pagerAdapter = PagerAdapter(this, details)
        with(pager) {
            adapter = pagerAdapter
            offscreenPageLimit = 3
            isUserInputEnabled = false
        }

        val tabsTitles = resources.getStringArray(R.array.freelancer_tabs)
        val tabsIcons = resources.getStringArray(R.array.freelancer_tabs_icons)

        TabLayoutMediator(tabs, pager, false) { tab, position ->
            tab.text = tabsTitles[position]
            tab.icon = ContextCompat.getDrawable(this, getDrawableIdByName(tabsIcons[position]))
            pager.setCurrentItem(tab.position, false)
        }.attach()
        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val view = pagerAdapter.getViewAtPosition(position)
                view?.let {
                    updatePagerHeightForChild(view, pager)
                }
            }
        })

        avatar.setUrl(details.attributes.avatar.large.url, isCircle = true)
        name.text = "${details.attributes.first_name} ${details.attributes.last_name}"
        login.text = details.attributes.login
        rating.text = details.attributes.rating.toString()
        verified.visibility = details.attributes.verification.identity.toVisibleState()
        isplus.visibility = details.attributes.is_plus_active.toVisibleState()
        status.text = details.attributes.status.name
        status.background.setColorFilter(
            ContextCompat.getColor(
                this,
                getColorByFreelancerStatus(getFreelancerStatus(details.attributes.status.id))
            ), PorterDuff.Mode.SRC_OVER
        )
        location.text = details.attributes.location?.let {
            if (details.attributes.location!!.country != null && details.attributes.location!!.city != null)
                "${details.attributes.location!!.country!!.name}, ${details.attributes.location!!.city!!.name}"
            else if (details.attributes.location!!.country != null)
                details.attributes.location!!.country!!.name else ""
        }
        if (details.attributes.location != null && details.attributes.location!!.country != null) {
            countryId = details.attributes.location!!.country!!.id
        }
        voteup.text = details.attributes.positive_reviews.toString()
        votedown.text = details.attributes.negative_reviews.toString()
        if (details.attributes.birth_date != null) {
            birthdate.text = details.attributes.birth_date!!.parseSimpleDate()
                ?.let { calculateAge(it).getEnding(this, R.array.ending_years) }
        } else birthdate.gone()

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
        snackbar(getString(R.string.no_internet_error_message), freelancerActivityContainer)
    }

    private inner class PagerAdapter(fa: FragmentActivity, details: FreelancerDetail.Data) :
        FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 3

        val fragments = arrayListOf<Fragment>(
            PagerFreelancerOverview.newInstance(details),
            PagerFreelancerOverview.newInstance(details),
            PagerFreelancerReviews.newInstance(details.id)
        )

        fun getViewAtPosition(position: Int) = fragments[position].view

        override fun createFragment(position: Int): Fragment = when (position) {
            0 -> fragments[0]
            1 -> fragments[1]
            2 -> fragments[2]
            else -> fragments[0]
        }
    }

}
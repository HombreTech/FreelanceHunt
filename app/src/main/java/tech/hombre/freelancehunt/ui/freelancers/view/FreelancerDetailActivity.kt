package tech.hombre.freelancehunt.ui.freelancers.view

import android.graphics.PorterDuff
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_freelancer_detail.*
import kotlinx.android.synthetic.main.placeholder_freelancer.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.domain.model.FreelancerDetail
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.common.EXTRA_2
import tech.hombre.freelancehunt.common.FreelancerStatus
import tech.hombre.freelancehunt.common.extensions.*
import tech.hombre.freelancehunt.ui.base.*
import tech.hombre.freelancehunt.ui.freelancers.presentation.FreelancerDetailViewModel
import tech.hombre.freelancehunt.ui.freelancers.presentation.FreelancerPublicViewModel
import tech.hombre.freelancehunt.ui.freelancers.view.pager.PagerFreelancerBids
import tech.hombre.freelancehunt.ui.freelancers.view.pager.PagerFreelancerOverview
import tech.hombre.freelancehunt.ui.freelancers.view.pager.PagerFreelancerReviews
import tech.hombre.freelancehunt.ui.menu.BottomMenuBuilder
import tech.hombre.freelancehunt.ui.menu.CreateThreadBottomDialogFragment


class FreelancerDetailActivity : BaseActivity(),
    CreateThreadBottomDialogFragment.OnCreateThreadListener {

    private val viewModel: FreelancerDetailViewModel by viewModel()

    private val freelancerPublicViewModel: FreelancerPublicViewModel by viewModel()

    var countryId = -1

    var profileId = -1

    var freelancerUrl = ""

    override fun viewReady() {
        setContentView(R.layout.activity_freelancer_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
            R.id.action_share -> shareUrl(this, freelancerUrl)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun subscribeToData() {
        viewModel.viewState.subscribe(this, ::handleViewState)
        viewModel.action.subscribe(this, ::handleActionViewState)
        viewModel.countries.subscribe(this, {
            hideLoading(progressBar)
            if (countryId != -1) {
                val country = it.find { it.id == countryId }
                if (country != null) locationIcon.setUrlSVG("https://freelancehunt.com/static/images/flags/4x3/${country.iso2.toLowerCase()}.svg")
            }
        })
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

    private fun handleActionViewState(viewState: ViewState<String>) {
        hideLoading(progressBar)
        when (viewState) {
            is Success -> {
                when (viewState.data) {
                    "message" -> toast(getString(R.string.message_sent))
                }
            }
        }
    }

    private fun initFreelancerDetails(details: FreelancerDetail.Data) {
        hideLoading(progressBar)

        preview.visibility = View.INVISIBLE
        content.visible()

        profileId = details.id

        freelancerUrl = details.links.self.web

        toolbar.subtitle = details.attributes.login


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

        visitedAt.text = details.attributes.visited_at?.parseFullDate(true).getTimeAgo()

        if (profileId != appPreferences.getCurrentUserId()) {
            buttonMessage.visible()
            if (getFreelancerStatus(details.attributes.status.id) != FreelancerStatus.TEMP_NOT_WORK) {
                buttonMessage.setOnClickListener {
                    BottomMenuBuilder(
                        supportFragmentManager,
                        CreateThreadBottomDialogFragment.TAG
                    ).buildMenuForCreateThread(profileId)
                }
            } else buttonMessage.isEnabled = false
        }

        supportFragmentManager.switch(
            R.id.fragmentContainer,
            PagerFreelancerOverview.newInstance(details),
            PagerFreelancerOverview.TAG,
            false
        )

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                containerScroller.scrollTo(0, 0)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                containerScroller.scrollTo(0, 0)
                tab?.position?.let {
                    when (it) {
                        0 -> supportFragmentManager.switch(
                            R.id.fragmentContainer,
                            PagerFreelancerOverview.newInstance(details),
                            PagerFreelancerOverview.TAG,
                            false
                        )
                        1 -> supportFragmentManager.switch(
                            R.id.fragmentContainer,
                            PagerFreelancerBids.newInstance(details.id),
                            PagerFreelancerBids.TAG,
                            false
                        )
                        2 -> supportFragmentManager.switch(
                            R.id.fragmentContainer,
                            PagerFreelancerReviews.newInstance(details.id),
                            PagerFreelancerReviews.TAG,
                            false
                        )
                    }
                }

            }

        })

    }


    private fun handleError(error: String) {
        hideLoading(progressBar)
        showError(error)
    }

    private fun showNoInternetError() {
        hideLoading(progressBar)
        snackbar(getString(R.string.no_internet_error_message), freelancerActivityContainer)
    }

    override fun onThreadCreated(subject: String, message: String, toProfileId: Int) {
        viewModel.sendMessage(toProfileId, subject, message)
    }

}
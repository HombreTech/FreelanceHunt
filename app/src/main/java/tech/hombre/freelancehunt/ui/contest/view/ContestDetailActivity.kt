package tech.hombre.freelancehunt.ui.contest.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_contest_detail.*
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

    private val viewModel: ContestDetailViewModel by viewModel()

    private val contestPublicViewModel: ContestPublicViewModel by viewModel()

    var contestId = 0

    var contestUrl = ""

    var countries = listOf<Countries.Data>()

    override fun viewReady() {
        setContentView(R.layout.activity_contest_detail)
        setSupportActionBar(toolbar)
        setTitle(R.string.contest_view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        intent?.extras?.let {
            contestId = it.getInt(EXTRA_1, -1)
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
            R.id.action_open -> openUrl(this, contestUrl)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun subscribeToData() {
        viewModel.viewState.subscribe(this, ::handleViewState)
        viewModel.countries.subscribe(this, {
            hideLoading(progressBar)
            countries = it
        })
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

    private fun initContestDetails(details: ContestDetail.Data) {
        hideLoading(progressBar)

        contestUrl = details.links.self.web

        toolbar.subtitle = details.attributes.name


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

        supportFragmentManager.switch(
            R.id.fragmentContainer,
            PagerContestOverview.newInstance(details.attributes),
            PagerContestOverview.TAG,
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
                            PagerContestOverview.newInstance(details.attributes),
                            PagerContestOverview.TAG,
                            false
                        )
                        1 -> supportFragmentManager.switch(
                            R.id.fragmentContainer,
                            PagerContestComments.newInstance(details.id),
                            PagerContestComments.TAG,
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
        snackbar(getString(R.string.no_internet_error_message), contestActivityContainer)
    }

    companion object {

        fun startActivity(context: Context, contextId: Int) {
            val intent = Intent(context, ContestDetailActivity::class.java)
            intent.putExtra(EXTRA_1, contextId)
            intent.putExtra(EXTRA_2, true)
            context.startActivity(intent)
        }
    }

}
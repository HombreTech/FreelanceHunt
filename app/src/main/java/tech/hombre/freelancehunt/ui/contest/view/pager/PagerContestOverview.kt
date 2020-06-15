package tech.hombre.freelancehunt.ui.contest.view.pager

import android.os.Bundle
import androidx.annotation.Keep
import kotlinx.android.synthetic.main.fragment_pager_project_overview.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter
import tech.hombre.domain.model.ContestDetail
import tech.hombre.domain.model.Countries
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.common.extensions.snackbar
import tech.hombre.freelancehunt.common.extensions.subscribe
import tech.hombre.freelancehunt.ui.base.*
import tech.hombre.freelancehunt.ui.contest.presentation.ContestOverviewViewModel
import tech.hombre.freelancehunt.ui.contest.presentation.ContestPublicViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class PagerContestOverview : BaseFragment() {
    override fun getLayout() = R.layout.fragment_pager_contest_overview

    var project: ContestDetail.Data.Attributes? = null

    private val contestPublicViewModel: ContestPublicViewModel by sharedViewModel()

    private val viewModel: ContestOverviewViewModel by viewModel()

    var countries = listOf<Countries.Data>()

    override fun viewReady() {
        arguments?.let {
            project = it.getParcelable(EXTRA_1)
            if (project != null) {
                subscribeToData()
                initOverview(project!!)
            }
        }
    }

    private fun subscribeToData() {
        viewModel.employerDetails.subscribe(this, {
            when (it) {
                is Loading -> showLoading()
                is Success -> {
                    hideLoading()
                    appNavigator.showEmployerDetails(it.data.data)
                }
                is Error -> handleError(it.error.localizedMessage)
                is NoInternetState -> showNoInternetError()
            }
        })
    }

    private fun handleError(error: String) {
        hideLoading()
        showError(error, overviewActivityContainer)
    }

    private fun showNoInternetError() {
        hideLoading()
        snackbar(getString(R.string.no_internet_error_message), overviewActivityContainer)
    }


    private fun initOverview(details: ContestDetail.Data.Attributes) {
        if (details.description_html != null) {
            description.setHtmlText(details.description_html!!)
        } else description.text = getString(R.string.no_information)

        avatar.setUrl(details.employer.avatar.large.url, isCircle = true)
        name.text = "${details.employer.first_name} ${details.employer.last_name}"
        login.text = details.employer.login

        buttonProfile.setOnClickListener {
            viewModel.getEmployerDetails(details.employer.id)
        }

    }

    companion object {
        @Keep
        val TAG = PagerContestOverview::class.java.simpleName

        fun newInstance(details: ContestDetail.Data.Attributes): PagerContestOverview {
            val fragment = PagerContestOverview()
            val extra = Bundle()
            extra.putParcelable(EXTRA_1, details)
            fragment.arguments = extra
            return fragment
        }

    }
}
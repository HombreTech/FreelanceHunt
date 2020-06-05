package tech.hombre.freelancehunt.ui.freelancers.view.pager

import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_pager_freelancer_overview.*
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter
import tech.hombre.domain.model.FreelancerDetail
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.ui.base.BaseFragment


class PagerFreelancerOverview : BaseFragment() {
    override fun getLayout() = R.layout.fragment_pager_freelancer_overview

    var details: FreelancerDetail.Data? = null

    override fun viewReady() {
        arguments?.let {
            details = it.getParcelable(EXTRA_1)
            if (details != null) {
                initOverview(details!!)
            }
        }
    }

    private fun initOverview(details: FreelancerDetail.Data) {
        if (!details.attributes.verification.phone) {
            verificatedPhone.alpha = 0.5f
        }
        if (!details.attributes.verification.birth_date) {
            verificatedBirth.alpha = 0.5f
        }
        if (!details.attributes.verification.website) {
            verificatedSite.alpha = 0.5f
        }
        if (!details.attributes.verification.wmid) {
            verificatedBankID.alpha = 0.5f
        }

        if (details.attributes.cv_html != null) {
            val getter = HtmlHttpImageGetter(summary, null, true).apply {
                enableCompressImage(true, 70)
            }
            summary.setHtml(details.attributes.cv_html!!, getter)
        } else summary.text = getString(R.string.no_information)
    }

    companion object {
        val TAG = PagerFreelancerOverview::class.java.simpleName

        fun newInstance(details: FreelancerDetail.Data): PagerFreelancerOverview {
            val fragment = PagerFreelancerOverview()
            val extra = Bundle()
            extra.putParcelable(EXTRA_1, details)
            fragment.arguments = extra
            return fragment
        }

    }
}
package tech.hombre.freelancehunt.ui.employers.view.pager

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.RelativeLayout
import androidx.annotation.Keep
import kotlinx.android.synthetic.main.fragment_pager_employer_overview.*
import tech.hombre.domain.model.EmployerDetail
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.ui.base.BaseFragment

class PagerEmployerOverview : BaseFragment() {
    override fun getLayout() = R.layout.fragment_pager_employer_overview

    var details: EmployerDetail.Data? = null

    override fun viewReady() {
        arguments?.let {
            details = it.getParcelable(EXTRA_1)
            if (details != null) {
                initOverview(details!!)
            }
        }
    }

    private fun initOverview(details: EmployerDetail.Data) {
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

            if (!summary.setHtmlText(details.attributes.cv_html!!)) {
                overviewFragmentContainer.removeView(summary)

                val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )

                val webView = WebView(requireContext())
                webView.layoutParams = params
                webView.apply {
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = false
                    settings.javaScriptCanOpenWindowsAutomatically = true
                    settings.mediaPlaybackRequiresUserGesture = true
                    webChromeClient = WebChromeClient()
                    loadDataWithBaseURL(
                        null,
                        details.attributes.cv_html ?: "",
                        "text/html",
                        "ru_RU",
                        null
                    )
                }

                overviewFragmentContainer.addView(webView)
            }
        } else summary.text = getString(R.string.no_information)
    }

    companion object {
        @Keep
        val TAG = PagerEmployerOverview::class.java.simpleName

        fun newInstance(details: EmployerDetail.Data): PagerEmployerOverview {
            val fragment = PagerEmployerOverview()
            val extra = Bundle()
            extra.putParcelable(EXTRA_1, details)
            fragment.arguments = extra
            return fragment
        }

    }
}
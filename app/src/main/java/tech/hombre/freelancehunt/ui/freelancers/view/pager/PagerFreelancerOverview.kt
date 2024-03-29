package tech.hombre.freelancehunt.ui.freelancers.view.pager

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.RelativeLayout
import androidx.annotation.Keep
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.vivchar.rendererrecyclerviewadapter.BaseViewRenderer
import com.github.vivchar.rendererrecyclerviewadapter.RendererRecyclerViewAdapter
import com.github.vivchar.rendererrecyclerviewadapter.ViewFinder
import com.github.vivchar.rendererrecyclerviewadapter.ViewRenderer
import kotlinx.android.synthetic.main.fragment_pager_freelancer_overview.*
import tech.hombre.domain.model.FreelancerDetail
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.common.UserType
import tech.hombre.freelancehunt.common.extensions.visible
import tech.hombre.freelancehunt.ui.base.BaseFragment


class PagerFreelancerOverview : BaseFragment() {
    override fun getLayout() = R.layout.fragment_pager_freelancer_overview

    var details: FreelancerDetail.Data? = null

    lateinit var adapter: RendererRecyclerViewAdapter

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

            if (!summary.setHtmlText(details.attributes.cv_html!!)) {
                val viewId = summary.id
                overviewFragmentContainer.removeView(summary)

                val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )

                val webView = WebView(requireContext())
                webView.layoutParams = params
                webView.apply {
                    id = viewId
                    layoutParams = params
                    webViewClient = object  : WebViewClient() {
                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): Boolean {
                            request?.let {
                                val url = request.url
                                openUrl(requireContext(), url.toString())
                            }
                            return true
                        }
                    }
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

        initSkills(details.attributes.skills)

        if (appPreferences.getCurrentUserType() == UserType.EMPLOYER.type) {
            buttonJob.visible()
            buttonJob.setOnClickListener {
                appNavigator.showNewProjectDialog(details.id, true, details.attributes.login)
            }
        }
    }

    private fun initSkills(skills: List<FreelancerDetail.Data.Attributes.Skill>) {
        adapter = RendererRecyclerViewAdapter()
        adapter.enableDiffUtil(true)
        adapter.registerRenderer(
            ViewRenderer(
                R.layout.item_skill_list,
                FreelancerDetail.Data.Attributes.Skill::class.java,
                BaseViewRenderer.Binder { model: FreelancerDetail.Data.Attributes.Skill, finder: ViewFinder, payloads: List<Any?>? ->
                    finder
                        .setText(R.id.name, model.name)
                        .setText(R.id.position, "#${model.rating_position}")
                }
            )
        )
        skillList.layoutManager = LinearLayoutManager(activity)
        skillList.adapter = adapter
        adapter.setItems(skills)
    }

    companion object {
        @Keep
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
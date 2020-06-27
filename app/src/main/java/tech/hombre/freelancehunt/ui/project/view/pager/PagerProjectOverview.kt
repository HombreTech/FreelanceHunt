package tech.hombre.freelancehunt.ui.project.view.pager

import android.os.Bundle
import androidx.annotation.Keep
import com.github.vivchar.rendererrecyclerviewadapter.BaseViewRenderer
import com.github.vivchar.rendererrecyclerviewadapter.RendererRecyclerViewAdapter
import com.github.vivchar.rendererrecyclerviewadapter.ViewFinder
import com.github.vivchar.rendererrecyclerviewadapter.ViewRenderer
import kotlinx.android.synthetic.main.fragment_pager_project_overview.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import tech.hombre.domain.model.Countries
import tech.hombre.domain.model.ProjectDetail
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.common.extensions.getTimeAgo
import tech.hombre.freelancehunt.common.extensions.parseFullDate
import tech.hombre.freelancehunt.common.extensions.snackbar
import tech.hombre.freelancehunt.common.extensions.subscribe
import tech.hombre.freelancehunt.ui.base.*
import tech.hombre.freelancehunt.ui.employers.presentation.EmployerDetailViewModel
import tech.hombre.freelancehunt.ui.project.presentation.ProjectPublicViewModel

class PagerProjectOverview : BaseFragment() {
    override fun getLayout() = R.layout.fragment_pager_project_overview

    var project: ProjectDetail.Data.Attributes? = null

    private val projectPublicViewModel: ProjectPublicViewModel by sharedViewModel()

    private val employerViewModel: EmployerDetailViewModel by sharedViewModel()

    var countries = listOf<Countries.Data>()

    lateinit var adapter: RendererRecyclerViewAdapter

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
        employerViewModel.viewState.subscribe(this, {
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


    private fun initOverview(details: ProjectDetail.Data.Attributes) {
        if (details.employer == null) {
            handleError(getString(R.string.only_for_plus))
            return
        }

        initSkills(details.skills)

        if (details.description_html != null) {
            val desc = StringBuilder()
            desc.append(details.description_html!!)

            details.updates.forEach { update ->
                desc.append("<b><i>")
                desc.append(getString(R.string.added))
                desc.append(update.published_at.parseFullDate(true).getTimeAgo())
                desc.append("</b></i>")
                desc.append("\n")
                desc.append(update.description_html)
            }

            description.setHtmlText(desc.toString())
        } else description.text = getString(R.string.no_information)

        avatar.setUrl(details.employer!!.avatar.large.url, isCircle = true)
        name.text = "${details.employer!!.first_name} ${details.employer!!.last_name}"
        login.text = details.employer!!.login

        buttonProfile.setOnClickListener {
            employerViewModel.getEmployerDetails(details.employer!!.id)
        }
    }

    private fun initSkills(skills: List<ProjectDetail.Data.Attributes.Skill>) {
        adapter = RendererRecyclerViewAdapter()
        adapter.enableDiffUtil(true)
        adapter.registerRenderer(
            ViewRenderer(
                R.layout.item_project_skill_list,
                ProjectDetail.Data.Attributes.Skill::class.java,
                BaseViewRenderer.Binder { model: ProjectDetail.Data.Attributes.Skill, finder: ViewFinder, payloads: List<Any?>? ->
                    finder
                        .setText(R.id.name, model.name)
                        .setOnClickListener {
                            handleError(getString(R.string.not_implemented_error))
                        }
                }
            )
        )
        skillList.adapter = adapter
        adapter.setItems(skills)
    }

    companion object {
        @Keep
        val TAG = PagerProjectOverview::class.java.simpleName

        fun newInstance(details: ProjectDetail.Data.Attributes): PagerProjectOverview {
            val fragment = PagerProjectOverview()
            val extra = Bundle()
            extra.putParcelable(EXTRA_1, details)
            fragment.arguments = extra
            return fragment
        }

    }
}
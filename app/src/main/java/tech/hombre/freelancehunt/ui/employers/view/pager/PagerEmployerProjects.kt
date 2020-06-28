package tech.hombre.freelancehunt.ui.employers.view.pager

import android.os.Bundle
import androidx.annotation.Keep
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.vivchar.rendererrecyclerviewadapter.*
import kotlinx.android.synthetic.main.fragment_pager_employer_projects.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.domain.model.ProjectDetail
import tech.hombre.domain.model.ProjectsList
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.common.SafeType
import tech.hombre.freelancehunt.common.UserType
import tech.hombre.freelancehunt.common.extensions.*
import tech.hombre.freelancehunt.common.widgets.CustomImageView
import tech.hombre.freelancehunt.common.widgets.EndlessScroll
import tech.hombre.freelancehunt.ui.base.*
import tech.hombre.freelancehunt.ui.base.ViewState
import tech.hombre.freelancehunt.ui.employers.presentation.EmployerProjectsViewModel

class PagerEmployerProjects : BaseFragment() {

    private val viewModel: EmployerProjectsViewModel by viewModel()

    override fun getLayout() = R.layout.fragment_pager_employer_projects

    lateinit var adapter: RendererRecyclerViewAdapter

    val items = arrayListOf<ProjectDetail.Data>()

    var profileId = 0

    override fun viewReady() {
        arguments?.let {
            profileId = it.getInt(EXTRA_1)
            if (profileId != 0) {
                initList()
                subscribeToData()
                viewModel.getProjectsLists(1, employerId = profileId)
            }
        }
    }

    private fun initList() {
        adapter = RendererRecyclerViewAdapter()
        adapter.enableDiffUtil(true)
        adapter.registerRenderer(
            ViewRenderer(
                R.layout.item_employer_project,
                ProjectDetail.Data::class.java,
                BaseViewRenderer.Binder { model: ProjectDetail.Data, finder: ViewFinder, payloads: List<Any?>? ->
                    finder
                        .setGone(R.id.budget, model.attributes.budget == null)
                        .setGone(R.id.premium, !model.attributes.is_premium)
                        .setGone(R.id.isPersonal, !model.attributes.is_personal)
                        .setGone(R.id.isplus, !model.attributes.is_only_for_plus)
                        .setText(
                            R.id.safe,
                            getTitleBySafeType(
                                requireContext(),
                                SafeType.values().find { it.type == model.attributes.safe_type }
                                    ?: SafeType.DIRECT_PAYMENT))
                        .setGone(R.id.isremote, !model.attributes.is_remote_job)
                        .setText(R.id.status, model.attributes.status.name)
                        .setText(R.id.name, model.attributes.name)
                        .setText(R.id.description, model.attributes.description.collapse(300))
                        .find(
                            R.id.avatar,
                            ViewProvider<CustomImageView> { avatar ->
                                avatar.setUrl(
                                    model.attributes.employer?.avatar?.small?.url ?: "",
                                    isCircle = true
                                )
                            })
                        .setText(
                            R.id.login,
                            model.attributes.employer?.let { if (it.first_name.isBlank()) it.login else it.first_name + " " + it.last_name }
                                ?: "-")
                        .setText(R.id.bidCount, model.attributes.bid_count.toString())
                        .setText(
                            R.id.budget,
                            if (model.attributes.budget != null) "${model.attributes.budget!!.amount} ${currencyToChar(
                                model.attributes.budget!!.currency
                            )}" else ""
                        )
                        .setOnClickListener(R.id.clickableView) {
                            val is_plus = appPreferences.getCurrentUserProfile()?.is_plus_active == true
                            if (appPreferences.getCurrentUserType() == UserType.FREELANCER.type) {
                                if (model.attributes.freelancer?.id == appPreferences.getCurrentUserId()) {
                                    appNavigator.showProjectDetails(model)
                                } else if (!model.attributes.is_personal && !model.attributes.is_only_for_plus) {
                                    appNavigator.showProjectDetails(model)
                                } else if (!model.attributes.is_personal && (model.attributes.is_only_for_plus && is_plus)) {
                                    appNavigator.showProjectDetails(model)
                                }
                            } else {
                               if (appPreferences.getCurrentUserId() == model.id) {
                                   appNavigator.showProjectDetails(model)
                                } else if (!model.attributes.is_personal && !model.attributes.is_only_for_plus) {
                                   appNavigator.showProjectDetails(model)
                               } else if (!model.attributes.is_personal && (model.attributes.is_only_for_plus && is_plus)) {
                                   appNavigator.showProjectDetails(model)
                               }
                            }
                        }
                }
            )
        )
        list.layoutManager = LinearLayoutManager(context)
        list.adapter = adapter
        adapter.registerRenderer(
            LoadMoreViewBinder(
                R.layout.item_load_more
            )
        )

        list.addOnScrollListener(object : EndlessScroll() {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (viewModel.pagination.next.isNotEmpty()) {
                    adapter.showLoadMore()
                    viewModel.getProjectsLists(page + 1, employerId = profileId)
                }
            }
        })
    }

    private fun subscribeToData() {
        viewModel.viewState.subscribe(this, ::handleViewState)
    }

    private fun handleViewState(viewState: ViewState<ProjectsList>) {
        when (viewState) {
            is Loading -> showLoading()
            is Success -> initProjectsList(viewState.data)
            is Error -> handleError(viewState.error.localizedMessage)
            is NoInternetState -> showNoInternetError()
        }
    }

    private fun handleError(error: String) {
        hideLoading()
        showError(error, projectsFragmentContainer)
    }

    private fun showNoInternetError() {
        hideLoading()
        snackbar(getString(R.string.no_internet_error_message), projectsFragmentContainer)
    }

    private fun initProjectsList(projectsList: ProjectsList) {
        hideLoading()
        items.addAll(projectsList.data)
        adapter.setItems(items)

        if (items.isEmpty()) {
            list.gone()
            empty.visible()
        }
    }

    companion object {
        @Keep
        val TAG = PagerEmployerProjects::class.java.simpleName

        fun newInstance(profileId: Int): PagerEmployerProjects {
            val fragment = PagerEmployerProjects()
            val extra = Bundle()
            extra.putInt(EXTRA_1, profileId)
            fragment.arguments = extra
            return fragment
        }

    }
}
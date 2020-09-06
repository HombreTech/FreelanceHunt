package tech.hombre.freelancehunt.ui.main.view.fragments

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.annotation.Keep
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.vivchar.rendererrecyclerviewadapter.*
import kotlinx.android.synthetic.main.fragment_projects.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.domain.model.ProjectDetail
import tech.hombre.domain.model.ProjectsList
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.SafeType
import tech.hombre.freelancehunt.common.extensions.*
import tech.hombre.freelancehunt.common.widgets.CustomImageView
import tech.hombre.freelancehunt.common.widgets.EndlessScroll
import tech.hombre.freelancehunt.ui.base.*
import tech.hombre.freelancehunt.ui.base.ViewState
import tech.hombre.freelancehunt.ui.main.presentation.MainPublicViewModel
import tech.hombre.freelancehunt.ui.main.presentation.ProjectsViewModel
import tech.hombre.freelancehunt.ui.menu.BottomMenuBuilder
import tech.hombre.freelancehunt.ui.menu.ProjectFilterBottomDialogFragment


class ProjectsFragment : BaseFragment(), ProjectFilterBottomDialogFragment.OnSubmitProjectFilter {

    private val viewModel: ProjectsViewModel by viewModel()

    private val publicViewModel: MainPublicViewModel by sharedViewModel()

    override fun getLayout() = R.layout.fragment_projects

    lateinit var adapter: RendererRecyclerViewAdapter

    val items = arrayListOf<ProjectDetail.Data>()

    override fun viewReady() {
        initList()
        subscribeToData()
        viewModel.getProjectsLists(1)
    }

    private fun initList() {
        adapter = RendererRecyclerViewAdapter()
        adapter.enableDiffUtil(true)
        adapter.registerRenderer(
            ViewRenderer(
                R.layout.item_project_list,
                ProjectDetail.Data::class.java,
                BaseViewRenderer.Binder { model: ProjectDetail.Data, finder: ViewFinder, payloads: List<Any?>? ->
                    finder
                        .setGone(R.id.budget, model.attributes.budget == null)
                        .setGone(R.id.premium, !model.attributes.is_premium)
                        .setText(
                            R.id.safe,
                            getTitleBySafeType(
                                requireContext(),
                                SafeType.values().find { it.type == model.attributes.safe_type }
                                    ?: SafeType.DIRECT_PAYMENT))
                        .setGone(R.id.isremote, !model.attributes.is_remote_job)
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
                        .setGone(R.id.isplus, !model.attributes.is_only_for_plus)
                        .setOnClickListener(R.id.clickableView) {
                            if (model.attributes.is_only_for_plus && appPreferences.getCurrentUserProfile()?.is_plus_active == true) {
                                appNavigator.showProjectDetails(model)
                            } else if (!model.attributes.is_only_for_plus)
                                appNavigator.showProjectDetails(model)
                            else
                                handleError(getString(R.string.only_for_plus))
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
                    viewModel.getProjectsLists(page)
                }
            }
        })

        refresh.setOnRefreshListener {
            refreshList()
        }

        restoreFilter()

    }

    private fun restoreFilter() {
        viewModel.onlyMySkills = appPreferences.getProjectFilterOnlyMySkills()
        viewModel.skills = if (appPreferences.getProjectFilterSkills()
                .isNotEmpty()
        ) appPreferences.getProjectFilterSkills().split(",").map { it.trim().toInt() }
            .toIntArray() else intArrayOf()
        viewModel.onlyForPlus = appPreferences.getProjectFilterOnlyPlus()
    }

    private fun subscribeToData() {
        viewModel.viewState.subscribe(this, ::handleViewState)
        publicViewModel.fabClickAction.subscribe(this, { action ->
            when (action) {
                "project_filter" -> showFilterDialog()
            }
        })
        viewModel.details.subscribe(this, {
            when (it) {
                is Loading -> showLoading()
                is Success -> {
                    hideLoading()
                    appNavigator.showProjectDetails(it.data.data)
                }
                is Error -> handleError(it.error.localizedMessage)
                is NoInternetState -> showNoInternetError()
            }
        })
    }

    private fun showFilterDialog() {
        BottomMenuBuilder(
            requireContext(),
            childFragmentManager,
            ProjectFilterBottomDialogFragment.TAG
        ).buildMenuForProjectFilter(
            viewModel.onlyMySkills,
            viewModel.skills,
            viewModel.onlyForPlus
        )
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
        refresh.isRefreshing = false
        hideLoading()
        showError(error, projectsFragmentContainer)
    }

    private fun showNoInternetError() {
        hideLoading()
        snackbar(getString(R.string.no_internet_error_message), projectsFragmentContainer)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_projects, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_filter -> {
                showFilterDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initProjectsList(projectsList: ProjectsList) {
        hideLoading()
        refresh.isRefreshing = false

        items.addAll(projectsList.data)
        adapter.setItems(items)

        if (items.isEmpty()) return

        appPreferences.setLastProjectId(items.first().id)
    }

    override fun onSubmitProjectFilter(
        onlyMySkills: Boolean,
        skills: IntArray,
        onlyForPlus: Boolean
    ) {
        viewModel.setProjectFilters(onlyMySkills, skills, onlyForPlus)
        appPreferences.saveProjectFilter(
            onlyMySkills,
            skills.joinToString(separator = ",") { it.toString() },
            onlyForPlus
        )
        refreshList()
    }

    private fun refreshList() {
        items.clear()
        adapter.setItems(items)
        viewModel.getProjectsLists(1)
    }

    companion object {
        @Keep
        val TAG = ProjectsFragment::class.java.simpleName

        fun newInstance() = ProjectsFragment()
    }
}
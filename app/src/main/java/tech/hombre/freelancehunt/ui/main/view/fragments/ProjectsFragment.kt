package tech.hombre.freelancehunt.ui.main.view.fragments

import androidx.recyclerview.widget.LinearLayoutManager
import com.github.vivchar.rendererrecyclerviewadapter.*
import kotlinx.android.synthetic.main.fragment_projects.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.domain.model.ProjectsList
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.extensions.snackbar
import tech.hombre.freelancehunt.common.extensions.subscribe
import tech.hombre.freelancehunt.common.widgets.CustomImageView
import tech.hombre.freelancehunt.common.widgets.EndlessScroll
import tech.hombre.freelancehunt.ui.base.*
import tech.hombre.freelancehunt.ui.base.ViewState
import tech.hombre.freelancehunt.ui.main.presentation.ProjectsViewModel
import tech.hombre.freelancehunt.ui.project.presentation.ProjectDetailViewModel

class ProjectsFragment : BaseFragment() {

    private val viewModel: ProjectsViewModel by viewModel()

    private val projectDetailsViewModel: ProjectDetailViewModel by sharedViewModel()

    override fun getLayout() = R.layout.fragment_projects

    lateinit var adapter: RendererRecyclerViewAdapter

    val items = arrayListOf<ProjectsList.Data>()

    override fun viewReady() {
        initList()
        subscribeToData()
        viewModel.getProjectsLists()
    }

    private fun initList() {
        adapter = RendererRecyclerViewAdapter()
        adapter.enableDiffUtil(true)
        adapter.registerRenderer(
            ViewRenderer(
                R.layout.item_project_list,
                ProjectsList.Data::class.java,
                BaseViewRenderer.Binder { model: ProjectsList.Data, finder: ViewFinder, payloads: List<Any?>? ->
                    finder
                        .setText(R.id.name, model.attributes.name)
                        .setGone(R.id.premium, !model.attributes.is_premium)
                        //.setGone(R.id.safe, !model.attributes.safe_type.isNotBlank())
                        .setGone(R.id.isremote, !model.attributes.is_remote_job)
                        .setText(R.id.description, model.attributes.description)
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
                                ?: "N/A")
                        .setText(R.id.bidCount, model.attributes.bid_count.toString())
                        .setGone(R.id.isplus, !model.attributes.is_only_for_plus)
                        .setOnClickListener(R.id.clickableView) {
                            projectDetailsViewModel.getProjectDetails(model.links.self.api)
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
                    viewModel.getProjectsLists(viewModel.pagination.next)
                }
            }
        })

        refresh.setOnRefreshListener {
            items.clear()
            adapter.setItems(items)
            viewModel.getProjectsLists()
        }
    }

    private fun subscribeToData() {
        viewModel.viewState.subscribe(this, ::handleViewState)
        projectDetailsViewModel.viewState.subscribe(this, {
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
        refresh.isRefreshing = false

        items.addAll(projectsList.data)
        adapter.setItems(items)
    }

    companion object {
        val TAG = ProjectsFragment::class.java.simpleName

        fun newInstance() = ProjectsFragment()
    }
}
package tech.hombre.freelancehunt.ui.main.view.fragments

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.annotation.Keep
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.vivchar.rendererrecyclerviewadapter.*
import kotlinx.android.synthetic.main.fragment_contests.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.domain.model.ContestDetail
import tech.hombre.domain.model.ContestsList
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.extensions.*
import tech.hombre.freelancehunt.common.widgets.CustomImageView
import tech.hombre.freelancehunt.common.widgets.EndlessScroll
import tech.hombre.freelancehunt.ui.base.*
import tech.hombre.freelancehunt.ui.base.ViewState
import tech.hombre.freelancehunt.ui.main.presentation.ContestsViewModel
import tech.hombre.freelancehunt.ui.main.presentation.MainPublicViewModel
import tech.hombre.freelancehunt.ui.menu.BottomMenuBuilder
import tech.hombre.freelancehunt.ui.menu.ContestsFilterBottomDialogFragment

class ContestsFragment : BaseFragment(), ContestsFilterBottomDialogFragment.OnSubmitContestsFilter {

    private val viewModel: ContestsViewModel by viewModel()

    private val publicViewModel: MainPublicViewModel by sharedViewModel()

    override fun getLayout() = R.layout.fragment_contests

    lateinit var adapter: RendererRecyclerViewAdapter

    val items = arrayListOf<ContestDetail.Data>()

    override fun viewReady() {
        initList()
        subscribeToData()
        viewModel.getContestsLists(1)
    }

    private fun subscribeToData() {
        viewModel.viewState.subscribe(this, ::handleViewState)
        publicViewModel.fabClickAction.subscribe(this, { action ->
            when (action) {
                "contest_filter" -> showFilterDialog()
            }
        })
        viewModel.details.subscribe(this, {
            when (it) {
                is Loading -> showLoading()
                is Success -> {
                    hideLoading()
                    appNavigator.showContestDetails(it.data.data)
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
            ContestsFilterBottomDialogFragment.TAG
        ).buildMenuForContestsFilter(
            viewModel.skills
        )
    }

    private fun handleViewState(viewState: ViewState<ContestsList>) {
        when (viewState) {
            is Loading -> showLoading()
            is Success -> initContestsList(viewState.data)
            is Error -> handleError(viewState.error.localizedMessage)
            is NoInternetState -> showNoInternetError()
        }
    }

    private fun handleError(error: String) {
        hideLoading()
        showError(error, contestsFragmentContainer)
    }

    private fun showNoInternetError() {
        hideLoading()
        snackbar(getString(R.string.no_internet_error_message), contestsFragmentContainer)
    }

    private fun initList() {
        adapter = RendererRecyclerViewAdapter()
        adapter.enableDiffUtil(true)
        adapter.registerRenderer(
            ViewRenderer(
                R.layout.item_contests_list,
                ContestDetail.Data::class.java,
                BaseViewRenderer.Binder { model: ContestDetail.Data, finder: ViewFinder, payloads: List<Any?>? ->
                    finder
                        .find(
                            R.id.icon,
                            ViewProvider<CustomImageView> { icon ->
                                icon.setUrl(
                                    "https://freelancehunt.com/static/images/contest/${getIconIdBySkillId(
                                        model.attributes.skill.id
                                    )}.png"
                                )
                            })
                        .setText(R.id.name, model.attributes.name)
                        .setText(R.id.skill, model.attributes.skill.name)
                        .setGone(R.id.isfinal, model.attributes.status.id != 140)
                        .setText(
                            R.id.budget,
                            "${model.attributes.budget.amount} ${currencyToChar(model.attributes.budget.currency)}"
                        )
                        .setText(
                            R.id.applications,
                            model.attributes.application_count.getEnding(
                                requireContext(),
                                R.array.ending_applications
                            )
                        )
                        .setText(
                            R.id.finalAt,
                            model.attributes.final_started_at.parseFullDate(true).getTimeAgo()
                        )
                        .setOnClickListener(R.id.clickableView) {
                            appNavigator.showContestDetails(model)
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
                    viewModel.getContestsLists(page)
                }
            }
        })

        refresh.setOnRefreshListener {
            refreshList()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //inflater.inflate(R.menu.menu_contests, menu)
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

    private fun initContestsList(contestsList: ContestsList) {
        hideLoading()
        refresh.isRefreshing = false

        items.addAll(contestsList.data)
        adapter.setItems(items)
    }

    override fun onSubmitContestsFilter(skills: IntArray) {
        viewModel.setContestsFilters(skills)
        refreshList()
    }

    private fun refreshList() {
        items.clear()
        adapter.setItems(items)
        viewModel.getContestsLists(1)
    }

    companion object {
        @Keep
        val TAG = ContestsFragment::class.java.simpleName

        fun newInstance() = ContestsFragment()
    }
}
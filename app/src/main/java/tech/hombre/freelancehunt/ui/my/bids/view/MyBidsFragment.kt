package tech.hombre.freelancehunt.ui.my.bids.view

import android.graphics.PorterDuff
import android.widget.TextView
import androidx.annotation.Keep
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.vivchar.rendererrecyclerviewadapter.*
import kotlinx.android.synthetic.main.fragment_threads.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.domain.model.MyBidsList
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.extensions.*
import tech.hombre.freelancehunt.common.widgets.EndlessScroll
import tech.hombre.freelancehunt.ui.base.*
import tech.hombre.freelancehunt.ui.base.ViewState
import tech.hombre.freelancehunt.ui.my.bids.presentation.MyBidsViewModel
import tech.hombre.freelancehunt.ui.project.presentation.ProjectDetailViewModel

class MyBidsFragment : BaseFragment() {

    private val viewModel: MyBidsViewModel by viewModel()

    lateinit var adapter: RendererRecyclerViewAdapter

    val items = arrayListOf<MyBidsList.Data>()

    override fun viewReady() {
        initList()
        subscribeToData()
        viewModel.getMyBids()
    }

    override fun getLayout() = R.layout.fragment_threads

    private fun subscribeToData() {
        viewModel.viewState.subscribe(this, ::handleViewState)
        viewModel.projectDetails.subscribe(this, {
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

    private fun handleViewState(viewState: ViewState<MyBidsList>) {
        when (viewState) {
            is Loading -> showLoading()
            is Success -> initMyBidsList(viewState.data)
            is Error -> handleError(viewState.error.localizedMessage)
            is NoInternetState -> showNoInternetError()
        }
    }

    private fun handleError(error: String) {
        hideLoading()
        showError(error, threadsFragmentContainer)
    }

    private fun showNoInternetError() {
        hideLoading()
        snackbar(getString(R.string.no_internet_error_message), threadsFragmentContainer)
    }

    private fun initList() {
        adapter = RendererRecyclerViewAdapter()
        adapter.enableDiffUtil(true)
        adapter.registerRenderer(
            ViewRenderer(
                R.layout.item_my_bids_list,
                MyBidsList.Data::class.java,
                BaseViewRenderer.Binder { model: MyBidsList.Data, finder: ViewFinder, payloads: List<Any?>? ->
                    finder
                        .find(R.id.status, ViewProvider<TextView> {
                            it.text = model.attributes.project.status.name
                            it.background.setColorFilter(
                                ContextCompat.getColor(
                                    requireContext(),
                                    getColorByProjectStatus(getProjectStatus(model.attributes.project.status.id))
                                ), PorterDuff.Mode.SRC_OVER
                            )
                        })
                        .find<TextView>(R.id.bidStatus) {
                            val status = getBidStatus(model.attributes.status)
                            it.text = getTitleByBidStatus(requireContext(), status)
                            it.background.setColorFilter(
                                ContextCompat.getColor(
                                    requireContext(),
                                    getColorByFreelancerStatus(status)
                                ), PorterDuff.Mode.SRC_OVER
                            )
                        }
                        .setText(R.id.name, model.attributes.project.name)
                        .setText(R.id.comment, model.attributes.comment)
                        .find(R.id.mybudget, ViewProvider<TextView> {
                            if (model.attributes.project.budget != null) {
                                it.text =
                                    "${model.attributes.project.budget!!.amount} ${currencyToChar(
                                        model.attributes.project.budget!!.currency
                                    )}"
                                it.visible()
                            } else it.gone()
                        })
                        .setText(
                            R.id.mybudget,
                            "${model.attributes.budget.amount} ${currencyToChar(model.attributes.budget.currency)}"
                        )
                        .setText(
                            R.id.days,
                            model.attributes.days.getEnding(requireContext(), R.array.ending_days)
                        )
                        .setOnClickListener(R.id.clickableView) {
                            viewModel.getProjectDetails(model.attributes.project.self)
                        }
                }
            )
        )
        list.layoutManager = LinearLayoutManager(activity)
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
                    viewModel.getMyBids(viewModel.pagination.next)
                }
            }
        })

        refresh.setOnRefreshListener {
            items.clear()
            adapter.setItems(items)
            viewModel.getMyBids()
        }
    }

    private fun initMyBidsList(freelancersList: MyBidsList) {
        hideLoading()
        refresh.isRefreshing = false

        items.addAll(freelancersList.data)
        adapter.setItems(items)
    }

    companion object {
        @Keep
        val TAG = MyBidsFragment::class.java.simpleName

        fun newInstance() = MyBidsFragment()
    }
}
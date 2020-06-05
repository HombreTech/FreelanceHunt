package tech.hombre.freelancehunt.ui.project.view.pager

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.vivchar.rendererrecyclerviewadapter.*
import kotlinx.android.synthetic.main.fragment_pager_project_bids.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.domain.model.ProjectBid
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.common.SafeType
import tech.hombre.freelancehunt.common.extensions.*
import tech.hombre.freelancehunt.common.widgets.CustomImageView
import tech.hombre.freelancehunt.ui.base.*
import tech.hombre.freelancehunt.ui.base.ViewState
import tech.hombre.freelancehunt.ui.freelancers.presentation.FreelancerDetailViewModel
import tech.hombre.freelancehunt.ui.project.presentation.ProjectBidsViewModel
import tech.hombre.freelancehunt.ui.project.presentation.ProjectPublicViewModel

class PagerProjectBids : BaseFragment() {
    override fun getLayout() = R.layout.fragment_pager_project_bids

    private val viewModel: ProjectBidsViewModel by viewModel()

    private val projectPublicViewModel: ProjectPublicViewModel by sharedViewModel()

    private val freelancerViewModel: FreelancerDetailViewModel by viewModel()

    lateinit var adapter: RendererRecyclerViewAdapter

    private var projectId = 0

    override fun viewReady() {
        arguments?.let {
            projectId = it.getInt(EXTRA_1)
            if (projectId != 0) {
                initList()
                subscribeToData()
                viewModel.getProjectBids(projectId)
            }
        }
    }

    private fun initList() {
        adapter = RendererRecyclerViewAdapter()
        adapter.enableDiffUtil(true)
        adapter.registerRenderer(
            ViewRenderer(
                R.layout.item_project_bids_list,
                ProjectBid.Data::class.java,
                BaseViewRenderer.Binder { model: ProjectBid.Data, finder: ViewFinder, payloads: List<Any?>? ->
                    if (model.attributes.is_hidden) {
                        finder
                            .setGone(R.id.clickableView, true)
                            .setGone(R.id.hiddenBid, false)
                    } else {
                        finder
                            .setGone(R.id.hiddenBid, true)
                            .setGone(R.id.clickableView, false)
                            .find(
                                R.id.avatar,
                                ViewProvider<CustomImageView> { avatar ->
                                    avatar.setUrl(
                                        model.attributes.freelancer.avatar.small.url,
                                        isCircle = true
                                    )
                                })
                            .setText(
                                R.id.login,
                                model.attributes.freelancer?.let { if (it.first_name.isBlank()) it.login else it.first_name + " " + it.last_name }
                                    ?: "N/A")
                            /*
                            TODO extend API
                            .setGone(R.id.verified, model.attributes.freelancer.is_verified)
                            .setGone(R.id.isplus, model.attributes.freelancer.is_plus)
                            */
                            .setText(R.id.comment, model.attributes.comment)
                            /*.find(R.id.budget, ViewProvider<TextView> {
                                if (model.attributes.project.budget != null) {
                                    it.text = "${model.attributes.project.budget!!.amount} ${currencyToChar(model.attributes.project.budget!!.currency)}"
                                    it.visible()
                                } else it.gone()
                            })*/
                            .setText(R.id.safe,
                                getTitleBySafeType(context!!, SafeType.values().find { it.type == model.attributes.safe_type } ?: SafeType.DIRECT_PAYMENT)
                            )
                            .setText(
                                R.id.days,
                                model.attributes.days.getEnding(context!!, R.array.ending_days)
                            )
                            .setText(
                                R.id.publishedAt,
                                model.attributes.published_at.parseFullDate(true).getTimeAgo()
                            )
                            .setText(
                                R.id.mybid,
                                "${model.attributes.budget.amount} ${currencyToChar(model.attributes.budget.currency)}"
                            )
                            /*
                            TODO extend API
                            .setText(R.id.rating, model.attributes.rating.toString())
                            .setText(R.id.voteup, model.attributes.positive_bids.toString())
                            .setText(R.id.votedown, model.attributes.negative_bids.toString())
                            */
                            .setInvisible(R.id.isWinner, !model.attributes.is_winner)
                            .setOnClickListener(R.id.clickableView) {
                                freelancerViewModel.getFreelancerDetails(model.attributes.freelancer.id)
                            }
                    }
                }
            )
        )
        list.layoutManager = LinearLayoutManager(activity)
        list.adapter = adapter
    }

    private fun subscribeToData() {
        viewModel.viewState.subscribe(this, ::handleViewState)
        freelancerViewModel.viewState.subscribe(this, {
            when (it) {
                is Loading -> showLoading()
                is Success -> {
                    hideLoading()
                    appNavigator.showFreelancerDetails(it.data.data)
                }
                is Error -> handleError(it.error.localizedMessage)
                is NoInternetState -> showNoInternetError()
            }
        })
    }

    private fun handleViewState(viewState: ViewState<ProjectBid>) {
        when (viewState) {
            is Loading -> showLoading()
            is Success -> initBids(viewState.data.data)
            is Error -> handleError(viewState.error.localizedMessage)
            is NoInternetState -> showNoInternetError()
        }
    }

    private fun handleError(error: String) {
        hideLoading()
        showError(error, bidsContainer)
    }

    private fun showNoInternetError() {
        hideLoading()
        snackbar(getString(R.string.no_internet_error_message), bidsContainer)
    }

    private fun initBids(bids: List<ProjectBid.Data>) {
        hideLoading()
        adapter.setItems(bids)
        projectPublicViewModel.updateBadge(1, bids.size)
    }

    companion object {
        val TAG = PagerProjectBids::class.java.simpleName

        fun newInstance(profileId: Int): PagerProjectBids {
            val fragment = PagerProjectBids()
            val extra = Bundle()
            extra.putInt(EXTRA_1, profileId)
            fragment.arguments = extra
            return fragment
        }

    }
}
package tech.hombre.freelancehunt.ui.project.view.pager

import android.graphics.PorterDuff
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.Keep
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.vivchar.rendererrecyclerviewadapter.*
import kotlinx.android.synthetic.main.fragment_pager_project_bids.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.ProjectBid
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.*
import tech.hombre.freelancehunt.common.extensions.*
import tech.hombre.freelancehunt.common.widgets.CustomImageView
import tech.hombre.freelancehunt.framework.app.ViewHelper.getColorAttr
import tech.hombre.freelancehunt.ui.base.*
import tech.hombre.freelancehunt.ui.base.ViewState
import tech.hombre.freelancehunt.ui.freelancers.presentation.FreelancerDetailViewModel
import tech.hombre.freelancehunt.ui.menu.BottomMenuBuilder
import tech.hombre.freelancehunt.ui.menu.ChooseBidBottomDialogFragment
import tech.hombre.freelancehunt.ui.menu.ListMenuBottomDialogFragment
import tech.hombre.freelancehunt.ui.menu.model.MenuItem
import tech.hombre.freelancehunt.ui.project.presentation.ProjectBidsViewModel
import tech.hombre.freelancehunt.ui.project.presentation.ProjectPublicViewModel
import java.util.*

class PagerProjectBids : BaseFragment(), ListMenuBottomDialogFragment.BottomListMenuListener,
    ChooseBidBottomDialogFragment.OnChooseBidListener {
    override fun getLayout() = R.layout.fragment_pager_project_bids

    private val viewModel: ProjectBidsViewModel by viewModel()

    private val projectPublicViewModel: ProjectPublicViewModel by sharedViewModel()

    private val freelancerViewModel: FreelancerDetailViewModel by viewModel()

    lateinit var adapter: RendererRecyclerViewAdapter

    private var projectId = 0

    private var employerId = 0

    private var items: ArrayList<ProjectBid.Data> = arrayListOf()

    override fun viewReady() {
        arguments?.let {
            projectId = it.getInt(EXTRA_1)
            employerId = it.getInt(EXTRA_2)
            if (projectId != 0 && employerId != 0) {
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
                    if (model.attributes.is_hidden && !(appPreferences.getCurrentUserType() == UserType.FREELANCER.type && model.attributes.freelancer.id == appPreferences.getCurrentUserId())) {
                        finder
                            .setGone(R.id.clickableView, true)
                            .setGone(R.id.hiddenBid, false)
                    } else {
                        finder
                            .setGone(R.id.hiddenBid, true)
                            .setGone(R.id.clickableView, false)
                            .find<CardView>(R.id.mainView) {
                                it.setCardBackgroundColor(if (appPreferences.getCurrentUserType() == UserType.FREELANCER.type && model.attributes.freelancer.id == appPreferences.getCurrentUserId()) getColorAttr(requireContext(), R.attr.myBidColor) else getColorAttr(requireContext(), R.attr.card_background))
                            }
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
                                getTitleBySafeType(
                                    requireContext(),
                                    SafeType.values().find { it.type == model.attributes.safe_type }
                                        ?: SafeType.DIRECT_PAYMENT)
                            )
                            .find<TextView>(R.id.status) {
                                val status = getBidStatus(model.attributes.status)
                                it.text = getTitleByBidStatus(requireContext(), status)
                                it.background.setColorFilter(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        getColorByFreelancerStatus(status)
                                    ), PorterDuff.Mode.SRC_OVER
                                )
                            }
                            .setText(
                                R.id.days,
                                model.attributes.days.getEnding(
                                    requireContext(),
                                    R.array.ending_days
                                )
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

                                val bidStatus = getBidStatus(model.attributes.status)
                                val isRevoked = bidStatus == BidStatus.REVOKED
                                val isRejected = bidStatus == BidStatus.REJECTED

                                val openForBids =
                                    getProjectStatus(model.attributes.project.status.id) == ProjectStatus.OPEN_FOR_PROPOSALS
                                if (!openForBids && appPreferences.getCurrentUserType() != UserType.FREELANCER.type) {
                                    freelancerViewModel.getFreelancerDetails(model.attributes.freelancer.id)
                                    return@setOnClickListener
                                }
                                if (appPreferences.getCurrentUserType() == UserType.FREELANCER.type) {
                                    if (model.attributes.freelancer.id == appPreferences.getCurrentUserId() && openForBids) {
                                        BottomMenuBuilder(
                                            requireContext(),
                                            childFragmentManager,
                                            ListMenuBottomDialogFragment.TAG
                                        ).buildMenuForFreelancerBid(
                                            model.attributes.project.id,
                                            model.id,
                                            isRevoked
                                        )
                                    } else freelancerViewModel.getFreelancerDetails(model.attributes.freelancer.id)
                                } else if (employerId == appPreferences.getCurrentUserId() && openForBids) {
                                    BottomMenuBuilder(
                                        requireContext(),
                                        childFragmentManager,
                                        ListMenuBottomDialogFragment.TAG
                                    ).buildMenuForEmployerBid(
                                        model.attributes.project.id,
                                        model.id,
                                        isRejected
                                    )

                                } else freelancerViewModel.getFreelancerDetails(model.attributes.freelancer.id)
                            }
                    }
                }
            )
        )
        list.layoutManager = LinearLayoutManager(activity)
        list.adapter = adapter

        bidsRevoked.setOnClickListener {
            setFilter("revoked")
        }
        bidsActive.setOnClickListener {
            setFilter("active")
        }
        bidsRejected.setOnClickListener {
            setFilter("rejected")
        }

    }

    private fun setFilter(status: String) {
        items.clear()
        adapter.setItems(items)
        viewModel.getProjectBids(projectId, status)
    }

    private fun subscribeToData() {
        viewModel.viewState.subscribe(this, ::handleViewState)
        viewModel.bid.subscribe(this, ::handleAddBid)
        viewModel.bidAction.subscribe(this, ::handleBidAction)
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

    private fun handleAddBid(viewState: ViewState<ProjectBid.Data>) {
        hideLoading()
        when (viewState) {
            is Success -> addBid(viewState.data)
        }
    }

    private fun handleBidAction(viewState: ViewState<Pair<Int, String>>) {
        hideLoading()
        when (viewState) {
            is Success -> {
                updateBid(viewState.data)
            }
        }
    }

    private fun updateBid(result: Pair<Int, String>) {
        val position = items.indexOf(items.find { it.id == result.first })
        when (result.second) {
            "choose" -> items[position].attributes.is_winner = true
            else -> items[position].attributes.status = result.second
        }
        adapter.notifyItemChanged(position)
    }

    private fun addBid(bid: ProjectBid.Data) {
        items.add(0, bid)
        adapter.setItems(items)
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
        items = bids as ArrayList<ProjectBid.Data>
        val reversed = appPreferences.getProjectBidsListReversed()
        val items = if (reversed) items.reversed() else items
        if ((appPreferences.getCurrentUserType() == UserType.FREELANCER.type)) {
            items.find { it.attributes.freelancer.id == appPreferences.getCurrentUserId() }?.let {
                Collections.swap(items, items.indexOf(it), 0)
            }
        }
        adapter.setItems(items)
    }

    fun onBidAdded(
        id: Int,
        days: Int,
        budget: MyBidsList.Data.Attributes.Budget,
        safeType: SafeType,
        comment: String,
        isHidden: Boolean
    ) {
        viewModel.addNewProjectBid(id, days, budget, safeType, comment, isHidden)
    }

    override fun onBidChoose(projectId: Int, bidId: Int, comment: String) {
        viewModel.chooseProjectBids(projectId, bidId, comment)
    }

    override fun onMenuItemSelected(
        primaryId: Int,
        secondaryId: Int,
        position: Int,
        model: MenuItem
    ) {
        when (model.tag) {
            "revoke" -> viewModel.revokeProjectBids(primaryId, secondaryId)
            "reject" -> viewModel.rejectProjectBids(primaryId, secondaryId)
            "restore" -> {
            }
            "choose" -> BottomMenuBuilder(
                requireContext(),
                childFragmentManager,
                ChooseBidBottomDialogFragment.TAG
            ).buildMenuForChooseBid(primaryId, secondaryId)
        }
    }

    companion object {
        @Keep
        val TAG = PagerProjectBids::class.java.simpleName

        fun newInstance(projectId: Int, employerId: Int): PagerProjectBids {
            val fragment = PagerProjectBids()
            val extra = Bundle()
            extra.putInt(EXTRA_1, projectId)
            extra.putInt(EXTRA_2, employerId)
            fragment.arguments = extra
            return fragment
        }

    }
}
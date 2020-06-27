package tech.hombre.freelancehunt.ui.my.workspaces.view

import android.widget.TextView
import androidx.annotation.Keep
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.vivchar.rendererrecyclerviewadapter.*
import kotlinx.android.synthetic.main.fragment_my_projects.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.domain.model.*
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.*
import tech.hombre.freelancehunt.common.extensions.*
import tech.hombre.freelancehunt.common.widgets.CustomImageView
import tech.hombre.freelancehunt.common.widgets.EndlessScroll
import tech.hombre.freelancehunt.ui.base.*
import tech.hombre.freelancehunt.ui.base.Success
import tech.hombre.freelancehunt.ui.base.ViewState
import tech.hombre.freelancehunt.ui.menu.*
import tech.hombre.freelancehunt.ui.menu.model.MenuItem
import tech.hombre.freelancehunt.ui.my.workspaces.presentation.MyWorkspacesViewModel

class MyWorkspacesFragment : BaseFragment(), ListMenuBottomDialogFragment.BottomListMenuListener,
    ProposeConditionsBottomDialogFragment.OnConditionsListener,
    SimpleInputBottomDialogFragment.OnDialogSubmitListener,
    CompleteWorkspaceBottomDialogFragment.OnCompleteDialogSubmitListener,
    ReviewWorkspaceBottomDialogFragment.OnReviewDialogSubmitListener {

    private val viewModel: MyWorkspacesViewModel by viewModel()

    override fun getLayout() = R.layout.fragment_my_workspaces

    lateinit var adapter: RendererRecyclerViewAdapter

    val items = arrayListOf<WorkspaceDetail.Data>()

    override fun viewReady() {
        initList()
        subscribeToData()
        viewModel.getMyWorkspacesLists()
    }

    private fun subscribeToData() {
        viewModel.viewState.subscribe(this, ::handleViewState)
        viewModel.workspaceAction.subscribe(this, ::handleWorkspaceAction)
    }

    private fun handleViewState(viewState: ViewState<WorkspacesList>) {
        when (viewState) {
            is Loading -> showLoading()
            is Success -> initMyWorkspacesList(viewState.data)
            is Error -> handleError(viewState.error.localizedMessage)
            is NoInternetState -> showNoInternetError()
        }
    }

    private fun handleWorkspaceAction(viewState: ViewState<Pair<Int, String>>) {
        hideLoading()
        when (viewState) {
            is Success -> {
                items.clear()
                //adapter.setItems(items)
                viewModel.getMyWorkspacesLists()
            }
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

    private fun initList() {
        adapter = RendererRecyclerViewAdapter()
        adapter.enableDiffUtil(true)
        adapter.registerRenderer(
            ViewRenderer(
                R.layout.item_workspaces_list,
                WorkspaceDetail.Data::class.java,
                BaseViewRenderer.Binder { model: WorkspaceDetail.Data, finder: ViewFinder, payloads: List<Any?>? ->
                    finder
                        .setText(R.id.name, model.attributes.project.name)
                        .setText(R.id.status, model.attributes.project.status.name)
                        .setText(R.id.safe, getTitleBySafeType(
                            requireContext(),
                            SafeType.values().find { it.type == model.attributes.project.safe_type }
                                ?: SafeType.DIRECT_PAYMENT))
                        .setText(
                            R.id.budget,
                            if (model.attributes.project.budget != null) "${model.attributes.project.budget!!.amount} ${currencyToChar(
                                model.attributes.project.budget!!.currency
                            )}" else ""
                        )
                        .setText(
                            R.id.expiredAt,
                            model.attributes.development_ends_at?.parseFullDate(true).getTimeAgo()
                        )
                        .find(
                            R.id.avatarEmployer,
                            ViewProvider<CustomImageView> { avatar ->
                                avatar.setUrl(
                                    model.attributes.employer?.avatar?.small?.url ?: "",
                                    isCircle = true
                                )
                            })
                        .find(
                            R.id.avatarFreelancer,
                            ViewProvider<CustomImageView> { avatar ->
                                avatar.setUrl(
                                    model.attributes.freelancer?.avatar?.small?.url ?: "",
                                    isCircle = true
                                )
                            })
                        .setText(
                            R.id.loginEmployer,
                            model.attributes.employer?.let { if (it.first_name.isBlank()) it.login else it.first_name + " " + it.last_name }
                                ?: "N/A")
                        .setText(
                            R.id.loginFreelancer,
                            model.attributes.freelancer?.let { if (it.first_name.isBlank()) it.login else it.first_name + " " + it.last_name }
                                ?: "N/A")
                        .setText(
                            R.id.budgetConfirmedEmployer,
                            if (model.attributes.project.budget != null) "${model.attributes.project.budget!!.amount} ${currencyToChar(
                                model.attributes.project.budget!!.currency
                            )}" else ""
                        )
                        .setText(
                            R.id.budgetConfirmedFreelancer,
                            if (model.attributes.project.budget != null) "${model.attributes.project.budget!!.amount} ${currencyToChar(
                                model.attributes.project.budget!!.currency
                            )}" else ""
                        )

                        .find(R.id.confirmedEmployer, ViewProvider<TextView> { textView ->
                            if (model.attributes.conditions.confirmed_by.employer) {
                                textView.compoundDrawablePadding = 8
                                textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                    ContextCompat.getDrawable(
                                        requireContext(),
                                        R.drawable.checkall
                                    ),
                                    null,
                                    null,
                                    null
                                )
                                textView.text = getString(R.string.confirmed)
                            } else textView.text = getString(R.string.not_confirmed)
                        })
                        .find(R.id.confirmedFreelancer, ViewProvider<TextView> { textView ->
                            if (model.attributes.conditions.confirmed_by.freelancer) {
                                textView.compoundDrawablePadding = 8
                                textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                    ContextCompat.getDrawable(
                                        requireContext(),
                                        R.drawable.checkall
                                    ),
                                    null,
                                    null,
                                    null
                                )
                                textView.text = getString(R.string.confirmed)
                            } else textView.text = getString(R.string.not_confirmed)
                        })
                        .setOnClickListener(R.id.loginEmployer) {
                            appNavigator.showEmployerDetails(model.attributes.employer.id)
                        }
                        .setOnClickListener(R.id.loginFreelancer) {
                            appNavigator.showFreelancerDetails(model.attributes.freelancer.id)
                        }
                        .setOnClickListener(R.id.clickableView) {
                            val projectStatus = getProjectStatus(model.attributes.project.status.id)
                            val isConfirmed =
                                if (appPreferences.getCurrentUserType() == UserType.EMPLOYER.type) model.attributes.conditions.confirmed_by.employer else model.attributes.conditions.confirmed_by.freelancer
                            BottomMenuBuilder(
                                requireContext(),
                                childFragmentManager,
                                ListMenuBottomDialogFragment.TAG
                            ).buildMenuForWorkspace(
                                model.id,
                                isConfirmed,
                                projectStatus,
                                SafeType.values()
                                    .find { it.type == model.attributes.project.safe_type } == SafeType.DIRECT_PAYMENT,
                                appPreferences.getCurrentUserType() == UserType.EMPLOYER.type
                            )
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
                    viewModel.getMyWorkspacesLists(viewModel.pagination.next)
                }
            }
        })

        refresh.setOnRefreshListener {
            items.clear()
            //adapter.setItems(items)
            viewModel.getMyWorkspacesLists()
        }
    }


    private fun initMyWorkspacesList(projectsList: WorkspacesList) {
        hideLoading()
        refresh.isRefreshing = false

        items.addAll(projectsList.data)
        adapter.setItems(items)
    }

    override fun onMenuItemSelected(
        primaryId: Int,
        secondaryId: Int,
        position: Int,
        model: MenuItem
    ) {
        when (model.tag) {
            "accept" -> {
                viewModel.acceptCondition(primaryId)
            }
            "reject" -> {
                viewModel.rejectCondition(primaryId)
            }
            "propose" -> {
                val item = items.find { it.id == primaryId }
                item?.let {
                    BottomMenuBuilder(
                        requireContext(),
                        childFragmentManager,
                        ProposeConditionsBottomDialogFragment.TAG
                    ).buildMenuForProposeConditions(
                        primaryId,
                        it.attributes.conditions.budget,
                        it.attributes.conditions.safe_type,
                        it.attributes.conditions.days
                    )
                }

            }
            "extend" -> {
                BottomMenuBuilder(
                    requireContext(),
                    childFragmentManager,
                    SimpleInputBottomDialogFragment.TAG
                ).buildMenuForExtendWorkspace(primaryId)
            }
            "arbitrage" -> {
                BottomMenuBuilder(
                    requireContext(),
                    childFragmentManager,
                    SimpleInputBottomDialogFragment.TAG
                ).buildMenuForRequestArbitrage(primaryId)
            }
            "complete" -> {
                BottomMenuBuilder(
                    requireContext(),
                    childFragmentManager,
                    CompleteWorkspaceBottomDialogFragment.TAG
                ).buildMenuForCompleteWorkspace(primaryId)
            }
            "incomplete" -> {
                BottomMenuBuilder(
                    requireContext(),
                    childFragmentManager,
                    CompleteWorkspaceBottomDialogFragment.TAG
                ).buildMenuForIncompleteWorkspace(primaryId)
            }
            "close_without_review" -> {
                BottomMenuBuilder(
                    requireContext(),
                    childFragmentManager,
                    SimpleInputBottomDialogFragment.TAG
                ).buildMenuForCloseWorkspace(primaryId)
            }
            "write_review" -> {
                BottomMenuBuilder(
                    requireContext(),
                    childFragmentManager,
                    ReviewWorkspaceBottomDialogFragment.TAG
                ).buildMenuForReviewWorkspace(primaryId)
            }
            "mailbox" -> {
                items.find { it.id == primaryId }?.let {
                    val threadId = it.id
                    val threadUrl = it.links.thread
                    appNavigator.showThread(threadId, threadUrl)
                }
            }
            "view" -> {
                items.find { it.id == primaryId }?.let {
                    appNavigator.showProjectDetails(it.attributes.project.id)
                }
            }
        }
    }

    override fun onConditionsChanged(
        id: Int,
        days: Int,
        budget: MyBidsList.Data.Attributes.Budget,
        safeType: SafeType,
        comment: String
    ) {
        viewModel.proposeCondition(id, days, budget, safeType, comment)
    }

    override fun onDialogSubmit(type: Int, primaryId: Int, text: String, digit: Int?) {
        when (type) {
            SIMPLE_DIALOG_REQUEST_ARBITRAGE -> viewModel.requestArbitrages(primaryId, text)
            SIMPLE_DIALOG_CLOSE_WORKSPACE -> viewModel.closeWorkspaces(primaryId, text)
            SIMPLE_DIALOG_EXTEND_WORKSPACE -> viewModel.extendCondition(primaryId, digit!!)
        }
    }

    override fun onCompleteDialogSubmit(
        primaryId: Int,
        isComplete: Boolean,
        review: String,
        grades: CompleteGrades
    ) =
        if (isComplete) {
            viewModel.completeWorkspaces(primaryId, grades, review)
        } else {
            viewModel.incompleteWorkspaces(primaryId, grades, review)
        }

    override fun onReviewDialogSubmit(primaryId: Int, review: String, grades: ReviewGrades) =
        viewModel.reviewWorkspaces(primaryId, grades, review)

    companion object {
        @Keep
        val TAG = MyWorkspacesFragment::class.java.simpleName

        fun newInstance() = MyWorkspacesFragment()
    }
}
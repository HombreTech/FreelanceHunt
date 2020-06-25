package tech.hombre.freelancehunt.ui.my.workspaces.view

import android.widget.TextView
import androidx.annotation.Keep
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.vivchar.rendererrecyclerviewadapter.*
import kotlinx.android.synthetic.main.fragment_my_projects.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.domain.model.WorkspaceDetail
import tech.hombre.domain.model.WorkspacesList
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.extensions.*
import tech.hombre.freelancehunt.common.widgets.CustomImageView
import tech.hombre.freelancehunt.common.widgets.EndlessScroll
import tech.hombre.freelancehunt.ui.base.*
import tech.hombre.freelancehunt.ui.base.ViewState
import tech.hombre.freelancehunt.ui.my.workspaces.presentation.MyWorkspacesViewModel

class MyWorkspacesFragment : BaseFragment() {

    private val viewModel: MyWorkspacesViewModel by viewModel()

    override fun getLayout() = R.layout.fragment_my_projects

    lateinit var adapter: RendererRecyclerViewAdapter

    val items = arrayListOf<WorkspaceDetail.Data>()

    override fun viewReady() {
        initList()
        subscribeToData()
        viewModel.getMyWorkspacesLists()
    }

    private fun subscribeToData() {
        viewModel.viewState.subscribe(this, ::handleViewState)
    }

    private fun handleViewState(viewState: ViewState<WorkspacesList>) {
        when (viewState) {
            is Loading -> showLoading()
            is Success -> initMyWorkspacesList(viewState.data)
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
                        .setText(
                            R.id.budget,
                            if (model.attributes.project.budget != null) "${model.attributes.project.budget!!.amount} ${currencyToChar(
                                model.attributes.project.budget!!.currency
                            )}" else ""
                        )
                        .setText(
                            R.id.expiredAt,
                            model.attributes.development_ends_at.parseFullDate(true).getTimeAgo()
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
                        .setText(R.id.budgetConfirmedEmployer, if (model.attributes.project.budget != null) "${model.attributes.project.budget!!.amount} ${currencyToChar(
                            model.attributes.project.budget!!.currency
                        )}" else "")
                        .setText(R.id.budgetConfirmedFreelancer, if (model.attributes.project.budget != null) "${model.attributes.project.budget!!.amount} ${currencyToChar(
                            model.attributes.project.budget!!.currency
                        )}" else "")

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
            adapter.setItems(items)
            viewModel.getMyWorkspacesLists()
        }
    }


    private fun initMyWorkspacesList(projectsList: WorkspacesList) {
        hideLoading()
        refresh.isRefreshing = false

        items.addAll(projectsList.data)
        adapter.setItems(items)
    }


    companion object {
        @Keep
        val TAG = MyWorkspacesFragment::class.java.simpleName

        fun newInstance() = MyWorkspacesFragment()
    }
}
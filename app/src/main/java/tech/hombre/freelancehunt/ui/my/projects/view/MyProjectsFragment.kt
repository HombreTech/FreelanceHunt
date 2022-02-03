package tech.hombre.freelancehunt.ui.my.projects.view

import android.app.DatePickerDialog
import androidx.annotation.Keep
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.vivchar.rendererrecyclerviewadapter.*
import kotlinx.android.synthetic.main.fragment_my_projects.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.domain.model.MyProjectsList
import tech.hombre.domain.model.ProjectDetail
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.ProjectStatus
import tech.hombre.freelancehunt.common.SafeType
import tech.hombre.freelancehunt.common.extensions.*
import tech.hombre.freelancehunt.common.widgets.CustomImageView
import tech.hombre.freelancehunt.common.widgets.EndlessScroll
import tech.hombre.freelancehunt.ui.base.*
import tech.hombre.freelancehunt.ui.base.ViewState
import tech.hombre.freelancehunt.ui.menu.BottomMenuBuilder
import tech.hombre.freelancehunt.ui.menu.ListMenuBottomDialogFragment
import tech.hombre.freelancehunt.ui.menu.model.MenuItem
import tech.hombre.freelancehunt.ui.my.projects.presentation.MyProjectSharedViewModel
import tech.hombre.freelancehunt.ui.my.projects.presentation.MyProjectsViewModel
import java.text.SimpleDateFormat
import java.util.*

class MyProjectsFragment : BaseFragment(), ListMenuBottomDialogFragment.BottomListMenuListener {

    private val viewModel: MyProjectsViewModel by viewModel()

    private val sharedViewModel: MyProjectSharedViewModel by viewModel()

    override fun getLayout() = R.layout.fragment_my_projects

    lateinit var adapter: RendererRecyclerViewAdapter

    val items = arrayListOf<ProjectDetail.Data>()

    override fun viewReady() {
        initList()
        subscribeToData()
        viewModel.getMyProjectsLists()

        addProjectButton.setOnClickListener {
            appNavigator.showNewProjectDialog()
        }
    }

    private fun subscribeToData() {
        viewModel.viewState.subscribe(this, ::handleViewState)
        viewModel.action.subscribe(this, ::handleActionViewState)
        sharedViewModel.action.subscribe(this, ::handleActionViewState)
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

    private fun handleViewState(viewState: ViewState<MyProjectsList>) {
        when (viewState) {
            is Loading -> showLoading()
            is Success -> initMyProjectsList(viewState.data)
            is Error -> handleError(viewState.error.localizedMessage)
            is NoInternetState -> showNoInternetError()
        }
    }

    private fun handleActionViewState(viewState: ViewState<Pair<Int, ProjectDetail?>>) {
        when (viewState) {
            is Success -> {
                hideLoading()
                val position = items.indexOf(items.find { it.id == viewState.data.first })
                if (viewState.data.second == null) {
                    items.clear()
                    //adapter.setItems(items)
                    viewModel.getMyProjectsLists()
                } else {
                    items[position] = viewState.data.second!!.data
                    adapter.notifyItemChanged(position)
                }
            }
        }
    }

    private fun handleActionViewState(viewState: Pair<Int, ProjectDetail>) {
        hideLoading()
        val position = items.indexOf(items.find { it.id == viewState.first })
        items[position] = viewState.second.data
        adapter.notifyItemChanged(position)
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
                                    ?: SafeType.EMPLOYER))
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
                        .setGone(R.id.isplus, !(model.attributes.is_only_for_plus ?: false))
                        .setOnClickListener(R.id.clickableView) {
                            BottomMenuBuilder(
                                requireContext(),
                                childFragmentManager,
                                ListMenuBottomDialogFragment.TAG
                            ).buildMenuForProject(
                                model.id,
                                model.attributes.bid_count > 0,
                                ProjectStatus.values().find { it.id == model.attributes.status.id } ?: ProjectStatus.OPEN_FOR_PROPOSALS
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
                    viewModel.getMyProjectsLists(viewModel.pagination.next)
                }
            }
        })

        refresh.setOnRefreshListener {
            items.clear()
            adapter.setItems(items)
            viewModel.getMyProjectsLists()
        }
    }


    private fun initMyProjectsList(projectsList: MyProjectsList) {
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
            "update" -> {
                appNavigator.showUpdateProjectDialog(
                    primaryId,
                    false,
                    items.find { it.id == primaryId })
            }
            "amend" -> {
                appNavigator.showUpdateProjectDialog(
                    primaryId,
                    true,
                    items.find { it.id == primaryId })
            }
            "extend" -> {
                val calendar: Calendar = Calendar.getInstance()
                val item = items.find { it.id == primaryId }
                calendar.time = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault()).parse(item?.attributes?.expired_at)
                val yy: Int = calendar.get(Calendar.YEAR)
                val mm: Int = calendar.get(Calendar.MONTH)
                val dd: Int = calendar.get(Calendar.DAY_OF_MONTH)

                val picker = DatePickerDialog(
                    requireContext(),
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                        val calendar = Calendar.getInstance()
                        calendar.set(year, month, dayOfMonth)

                        val format =
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
                        val endDate = format.format(calendar.time)

                        viewModel.extendProjects(primaryId, endDate)
                    },
                    yy,
                    mm,
                    dd
                )
                picker.datePicker.minDate = System.currentTimeMillis() - 1000
                picker.show()
            }
            "view" -> appNavigator.showProjectDetails(items.find { it.id == primaryId }!!)
            "close" -> {
                viewModel.closeProjects(primaryId)
            }
            "reopen" -> {
                val calendar: Calendar = Calendar.getInstance()
                val yy: Int = calendar.get(Calendar.YEAR)
                val mm: Int = calendar.get(Calendar.MONTH)
                val dd: Int = calendar.get(Calendar.DAY_OF_MONTH)

                val picker = DatePickerDialog(
                    requireContext(),
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                        val calendar = Calendar.getInstance()
                        calendar.set(year, month, dayOfMonth)

                        val format =
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
                        val endDate = format.format(calendar.time)

                        viewModel.reopenProjects(primaryId, endDate)
                    },
                    yy,
                    mm,
                    dd
                )
                picker.datePicker.minDate = System.currentTimeMillis() - 1000
                picker.show()
            }
        }
    }

    companion object {
        @Keep
        val TAG = MyProjectsFragment::class.java.simpleName

        fun newInstance() = MyProjectsFragment()
    }
}
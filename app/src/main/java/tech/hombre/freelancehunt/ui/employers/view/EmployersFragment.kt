package tech.hombre.freelancehunt.ui.employers.view

import android.widget.TextView
import androidx.annotation.Keep
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.vivchar.rendererrecyclerviewadapter.*
import kotlinx.android.synthetic.main.fragment_employers.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.domain.model.Countries
import tech.hombre.domain.model.EmployerDetail
import tech.hombre.domain.model.EmployersList
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.extensions.*
import tech.hombre.freelancehunt.common.widgets.CustomImageView
import tech.hombre.freelancehunt.common.widgets.EndlessScroll
import tech.hombre.freelancehunt.ui.base.*
import tech.hombre.freelancehunt.ui.base.ViewState
import tech.hombre.freelancehunt.ui.employers.presentation.EmployerDetailViewModel
import tech.hombre.freelancehunt.ui.employers.presentation.EmployersViewModel

class EmployersFragment : BaseFragment() {

    override fun getLayout() = R.layout.fragment_employers

    private val viewModel: EmployersViewModel by viewModel()

    lateinit var adapter: RendererRecyclerViewAdapter

    val items = arrayListOf<EmployerDetail.Data>()

    var countries = listOf<Countries.Data>()

    override fun viewReady() {
        initList()
        subscribeToData()
        viewModel.getEmployers()
    }

    private fun subscribeToData() {
        viewModel.viewState.subscribe(this, ::handleViewState)
        viewModel.countries.subscribe(this, {
            countries = it
        })
        viewModel.details.subscribe(this, {
            when (it) {
                is Loading -> showLoading()
                is Success -> {
                    hideLoading()
                    appNavigator.showEmployerDetails(it.data.data)
                }
                is Error -> handleError(it.error.localizedMessage)
                is NoInternetState -> showNoInternetError()
            }
        })
        viewModel.setCountries()
    }

    private fun handleViewState(viewState: ViewState<EmployersList>) {
        when (viewState) {
            is Loading -> showLoading()
            is Success -> initEmployersList(viewState.data)
            is Error -> handleError(viewState.error.localizedMessage)
            is NoInternetState -> showNoInternetError()
        }
    }

    private fun handleError(error: String) {
        hideLoading()
        showError(error, employersFragmentContainer)
    }

    private fun showNoInternetError() {
        hideLoading()
        snackbar(getString(R.string.no_internet_error_message), employersFragmentContainer)
    }


    private fun initList() {
        adapter = RendererRecyclerViewAdapter()
        adapter.enableDiffUtil(true)
        adapter.registerRenderer(
            ViewRenderer(
                R.layout.item_employers_list,
                EmployerDetail.Data::class.java,
                BaseViewRenderer.Binder { model: EmployerDetail.Data, finder: ViewFinder, payloads: List<Any?>? ->
                    finder
                        .setGone(R.id.verified, !model.attributes.verification.identity)
                        .setGone(R.id.isplus, !model.attributes.is_plus_active)
                        .setText(R.id.rating, model.attributes.rating.toString())
                        .setText(R.id.voteup, model.attributes.positive_reviews.toString())
                        .setText(R.id.votedown, model.attributes.negative_reviews.toString())
                        .find(
                            R.id.avatar,
                            ViewProvider<CustomImageView> { avatar ->
                                avatar.setUrl(
                                    model.attributes.avatar.large.url,
                                    isCircle = true
                                )
                            })
                        .setText(
                            R.id.name,
                            "${model.attributes.first_name} ${model.attributes.last_name}"
                        )
                        .find(R.id.comment, ViewProvider<TextView> {
                            if (model.attributes.cv != null) {
                                it.text = model.attributes.cv!!.collapse(300)
                                it.visible()
                            } else it.gone()
                        })
                        .find(R.id.location, ViewProvider<TextView> {
                            it.text = model.attributes.location?.let {
                                if (model.attributes.location!!.country != null && model.attributes.location!!.city != null)
                                    "${model.attributes.location!!.country!!.name}, ${model.attributes.location!!.city!!.name}"
                                else if (model.attributes.location!!.country != null)
                                    model.attributes.location!!.country!!.name else ""
                            }
                        })
                        .find(R.id.locationIcon, ViewProvider<CustomImageView> { locationIcon ->
                            if (model.attributes.location != null && model.attributes.location!!.country != null) {
                                val country =
                                    countries.find { it.id == model.attributes.location!!.country!!.id }
                                if (country != null) locationIcon.setUrlSVG("https://freelancehunt.com/static/images/flags/4x3/${country.iso2.toLowerCase()}.svg")
                            }
                        })
                        .setText(
                            R.id.serviceOn,
                            model.attributes.created_at.parseFullDate(
                                true
                            ).getSimpleTimeAgo(requireContext())
                        )
                        .setOnClickListener(R.id.clickableView) {
                            appNavigator.showEmployerDetails(model)
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
                if (items.isNotEmpty() && viewModel.pagination.next.isNotEmpty()) {
                    adapter.showLoadMore()
                    viewModel.getEmployers(viewModel.pagination.next)
                }
            }
        })

        refresh.setOnRefreshListener {
            items.clear()
            adapter.setItems(items)
            viewModel.getEmployers()
        }
    }

    private fun initEmployersList(freelancersList: EmployersList) {
        hideLoading()
        refresh.isRefreshing = false

        items.addAll(freelancersList.data)
        adapter.setItems(items)
    }

    companion object {
        @Keep
        val TAG = EmployersFragment::class.java.simpleName

        fun newInstance() = EmployersFragment()
    }
}
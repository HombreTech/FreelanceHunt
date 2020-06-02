package tech.hombre.freelancehunt.ui.freelancers.view

import android.graphics.PorterDuff
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.vivchar.rendererrecyclerviewadapter.*
import kotlinx.android.synthetic.main.fragment_freelancers.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.domain.model.Countries
import tech.hombre.domain.model.FreelancersList
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.extensions.*
import tech.hombre.freelancehunt.common.widgets.CustomImageView
import tech.hombre.freelancehunt.common.widgets.EndlessScroll
import tech.hombre.freelancehunt.ui.base.*
import tech.hombre.freelancehunt.ui.base.ViewState
import tech.hombre.freelancehunt.ui.freelancers.presentation.FreelancerDetailViewModel
import tech.hombre.freelancehunt.ui.freelancers.presentation.FreelancersViewModel

class FreelancersFragment : BaseFragment() {

    override fun getLayout() = R.layout.fragment_freelancers

    private val viewModel: FreelancersViewModel by viewModel()

    lateinit var adapter: RendererRecyclerViewAdapter

    val items = arrayListOf<FreelancersList.Data>()

    var countries = listOf<Countries.Data>()

    override fun viewReady() {
        initList()
        subscribeToData()
        viewModel.getFreelancers()
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
                    appNavigator.showFreelancerDetails(it.data.data)
                }
                is Error -> handleError(it.error.localizedMessage)
                is NoInternetState -> showNoInternetError()
            }
        })

        viewModel.setCountries()
    }

    private fun handleViewState(viewState: ViewState<FreelancersList>) {
        when (viewState) {
            is Loading -> showLoading()
            is Success -> initFreelancersList(viewState.data)
            is Error -> handleError(viewState.error.localizedMessage)
            is NoInternetState -> showNoInternetError()
        }
    }

    private fun handleError(error: String) {
        hideLoading()
        showError(error, freelancersFragmentContainer)
    }

    private fun showNoInternetError() {
        hideLoading()
        snackbar(getString(R.string.no_internet_error_message), freelancersFragmentContainer)
    }


    private fun initList() {
        adapter = RendererRecyclerViewAdapter()
        adapter.enableDiffUtil(true)
        adapter.registerRenderer(
            ViewRenderer(
                R.layout.item_freelancers_list,
                FreelancersList.Data::class.java,
                BaseViewRenderer.Binder { model: FreelancersList.Data, finder: ViewFinder, payloads: List<Any?>? ->
                    finder
                        .setGone(R.id.verified, !model.attributes.verification.identity)
                        .find(R.id.status, ViewProvider<TextView> {
                            it.text = model.attributes.status.name
                            it.background.setColorFilter(
                                ContextCompat.getColor(
                                    context!!,
                                    getColorByFreelancerStatus(getFreelancerStatus(model.attributes.status.id))
                                ), PorterDuff.Mode.SRC_OVER
                            )
                        })
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
                        .setText(
                            R.id.skill,
                            model.attributes.skills.joinToString(separator = ", ") { it.name })
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
                            ).getSimpleTimeAgo(context!!)
                        )
                        .setOnClickListener(R.id.clickableView) {
                            viewModel.getFreelancerDetails(model.id)
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
                    viewModel.getFreelancers(viewModel.pagination.next)
                }
            }
        })

        refresh.setOnRefreshListener {
            items.clear()
            adapter.setItems(items)
            viewModel.getFreelancers()
        }
    }

    private fun initFreelancersList(freelancersList: FreelancersList) {
        hideLoading()
        refresh.isRefreshing = false

        items.addAll(freelancersList.data)
        adapter.setItems(items)
    }

    companion object {
        val TAG = FreelancersFragment::class.java.simpleName

        fun newInstance() = FreelancersFragment()
    }
}
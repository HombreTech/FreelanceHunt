package tech.hombre.freelancehunt.ui.employers.view.pager

import android.os.Bundle
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.vivchar.rendererrecyclerviewadapter.*
import kotlinx.android.synthetic.main.fragment_pager_employer_reviews.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.domain.model.EmployerReviews
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.common.extensions.*
import tech.hombre.freelancehunt.common.widgets.CustomImageView
import tech.hombre.freelancehunt.common.widgets.EndlessScroll
import tech.hombre.freelancehunt.ui.base.*
import tech.hombre.freelancehunt.ui.base.ViewState
import tech.hombre.freelancehunt.ui.employers.presentation.EmployerDetailViewModel
import tech.hombre.freelancehunt.ui.employers.presentation.EmployerPublicViewModel
import tech.hombre.freelancehunt.ui.employers.presentation.EmployerReviewsViewModel
import tech.hombre.freelancehunt.ui.project.presentation.ProjectDetailViewModel

class PagerEmployerReviews : BaseFragment() {
    override fun getLayout() = R.layout.fragment_pager_employer_reviews

    private val viewModel: EmployerReviewsViewModel by viewModel()

    private val projectPublicViewModel: EmployerPublicViewModel by sharedViewModel()

    lateinit var adapter: RendererRecyclerViewAdapter

    val items = arrayListOf<EmployerReviews.Data>()

    private var profileId = 0

    override fun viewReady() {
        arguments?.let {
            profileId = it.getInt(EXTRA_1)
            if (profileId != 0) {
                initList()
                subscribeToData()
                viewModel.getEmployerReview("employers/$profileId/reviews")
            }
        }
    }

    private fun subscribeToData() {
        viewModel.viewState.subscribe(this, ::handleViewState)
        viewModel.employerDetails.subscribe(this, {
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

    private fun handleViewState(viewState: ViewState<EmployerReviews>) {
        when (viewState) {
            is Loading -> showLoading()
            is Success -> initEmployerReviews(viewState.data.data)
            is Error -> handleError(viewState.error.localizedMessage)
            is NoInternetState -> showNoInternetError()
        }
    }

    private fun initEmployerReviews(reviews: List<EmployerReviews.Data>) {
        hideLoading()
        items.addAll(reviews)
        adapter.setItems(items)
        projectPublicViewModel.updateBadge(2, reviews.size)
    }

    private fun initList() {
        adapter = RendererRecyclerViewAdapter()
        adapter.enableDiffUtil(true)
        adapter.registerRenderer(
            ViewRenderer(
                R.layout.item_employers_reviews_list,
                EmployerReviews.Data::class.java,
                BaseViewRenderer.Binder { model: EmployerReviews.Data, finder: ViewFinder, payloads: List<Any?>? ->
                    finder
                        .setText(R.id.projectName, model.attributes.project.name)
                        .setText(
                            R.id.publishedAt,
                            model.attributes.published_at.parseFullDate(true)
                                .getSimpleTimeAgo(context!!)
                        )
                        .find<RatingBar>(R.id.payBar) {
                            it.rating = (model.attributes.grades.pay ?: 0).toFloat()
                        }
                        .find<RatingBar>(R.id.requirementsBar) {
                            it.rating = (model.attributes.grades.requirements ?: 0).toFloat()
                        }.find<RatingBar>(R.id.definitionBar) {
                            it.rating = (model.attributes.grades.definition ?: 0).toFloat()
                        }.find<RatingBar>(R.id.connectivityBar) {
                            it.rating = (model.attributes.grades.connectivity ?: 0).toFloat()
                        }
                        .find(
                            R.id.avatar,
                            ViewProvider<CustomImageView> { avatar ->
                                avatar.setUrl(
                                    model.attributes.from.avatar.small.url,
                                    isCircle = true
                                )
                            })
                        .setText(
                            R.id.name,
                            "${model.attributes.from.first_name} ${model.attributes.from.last_name}"
                        )
                        .setText(R.id.comment, model.attributes.comment)
                        .find(R.id.budget, ViewProvider<TextView> {
                            if (model.attributes.project.budget != null) {
                                it.text =
                                    "${model.attributes.project.budget!!.amount} ${currencyToChar(
                                        model.attributes.project.budget!!.currency
                                    )}"
                                it.visible()
                            } else it.gone()
                        })
                        .setOnClickListener(R.id.clickableView) {
                            viewModel.getProjectDetails(model.attributes.project.self)
                        }
                        .setOnClickListener(R.id.name) {
                            viewModel.getEmployerDetails(model.attributes.from.id)
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
                    println("showLoadMore")
                    //adapter.showLoadMore()
                    //viewModel.getFreelancerReview(viewModel.pagination.next)
                }
            }
        })
    }

    private fun handleError(error: String) {
        hideLoading()
        showError(error, reviewsContainer)
    }

    private fun showNoInternetError() {
        hideLoading()
        snackbar(getString(R.string.no_internet_error_message), reviewsContainer)
    }

    companion object {
        val TAG = PagerEmployerReviews::class.java.simpleName

        fun newInstance(profileId: Int): PagerEmployerReviews {
            val fragment = PagerEmployerReviews()
            val extra = Bundle()
            extra.putInt(EXTRA_1, profileId)
            fragment.arguments = extra
            return fragment
        }

    }
}
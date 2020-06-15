package tech.hombre.freelancehunt.ui.freelancers.view.pager

import android.os.Bundle
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.Keep
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.vivchar.rendererrecyclerviewadapter.*
import kotlinx.android.synthetic.main.fragment_pager_freelancer_reviews.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.domain.model.FreelancerReviews
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.common.extensions.*
import tech.hombre.freelancehunt.common.widgets.CustomImageView
import tech.hombre.freelancehunt.common.widgets.EndlessScroll
import tech.hombre.freelancehunt.ui.base.*
import tech.hombre.freelancehunt.ui.base.ViewState
import tech.hombre.freelancehunt.ui.freelancers.presentation.FreelancerPublicViewModel
import tech.hombre.freelancehunt.ui.freelancers.presentation.FreelancerReviewsViewModel

class PagerFreelancerReviews : BaseFragment() {
    override fun getLayout() = R.layout.fragment_pager_freelancer_reviews

    private val viewModel: FreelancerReviewsViewModel by viewModel()

    private val projectPublicViewModel: FreelancerPublicViewModel by sharedViewModel()

    lateinit var adapter: RendererRecyclerViewAdapter

    val items = arrayListOf<FreelancerReviews.Data>()

    private var profileId = 0

    override fun viewReady() {
        arguments?.let {
            profileId = it.getInt(EXTRA_1)
            if (profileId != 0) {
                initList()
                subscribeToData()
                viewModel.getFreelancerReview("freelancers/$profileId/reviews")
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

    private fun handleViewState(viewState: ViewState<FreelancerReviews>) {
        when (viewState) {
            is Loading -> showLoading()
            is Success -> initFreelancerReviews(viewState.data.data)
            is Error -> handleError(viewState.error.localizedMessage)
            is NoInternetState -> showNoInternetError()
        }
    }

    private fun initFreelancerReviews(reviews: List<FreelancerReviews.Data>) {
        hideLoading()
        items.addAll(reviews)
        adapter.setItems(items)
    }

    private fun initList() {
        adapter = RendererRecyclerViewAdapter()
        adapter.enableDiffUtil(true)
        adapter.registerRenderer(
            ViewRenderer(
                R.layout.item_freelancers_reviews_list,
                FreelancerReviews.Data::class.java,
                BaseViewRenderer.Binder { model: FreelancerReviews.Data, finder: ViewFinder, payloads: List<Any?>? ->
                    finder
                        .find(R.id.projectName, ViewProvider<TextView> { projectName ->
                            projectName.text = model.attributes.project.name
                            projectName.compoundDrawablePadding = 8
                            val rateIcon = ContextCompat.getDrawable(requireContext(), if (model.attributes.grades.total ?: 0f < 6) R.drawable.thumb_down else R.drawable.thumb_up)
                            projectName.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                rateIcon,
                                null,
                                null,
                                null
                            )
                        })
                        .setText(
                            R.id.publishedAt,
                            model.attributes.published_at.parseFullDate(true)
                                .getSimpleTimeAgo(requireContext())
                        )
                        .find<RatingBar>(R.id.qualityBar) {
                            it.rating = (model.attributes.grades.quality ?: 0).toFloat()
                        }
                        .find<RatingBar>(R.id.professionalismBar) {
                            it.rating = (model.attributes.grades.professionalism ?: 0).toFloat()
                        }.find<RatingBar>(R.id.costBar) {
                            it.rating = (model.attributes.grades.cost ?: 0).toFloat()
                        }.find<RatingBar>(R.id.connectivityBar) {
                            it.rating = (model.attributes.grades.connectivity ?: 0).toFloat()
                        }
                        .find<RatingBar>(R.id.scheduleBar) {
                            it.rating = (model.attributes.grades.schedule ?: 0).toFloat()
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
                    adapter.showLoadMore()
                    viewModel.getFreelancerReview(viewModel.pagination.next)
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
        @Keep
        val TAG = PagerFreelancerReviews::class.java.simpleName

        fun newInstance(profileId: Int): PagerFreelancerReviews {
            val fragment = PagerFreelancerReviews()
            val extra = Bundle()
            extra.putInt(EXTRA_1, profileId)
            fragment.arguments = extra
            return fragment
        }

    }
}
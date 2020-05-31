package tech.hombre.freelancehunt.ui.main.view.fragments

import android.graphics.PorterDuff
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.vivchar.rendererrecyclerviewadapter.*
import kotlinx.android.synthetic.main.fragment_feed.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.domain.model.FeedList
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.extensions.*
import tech.hombre.freelancehunt.common.widgets.CustomImageView
import tech.hombre.freelancehunt.ui.base.*
import tech.hombre.freelancehunt.ui.base.ViewState
import tech.hombre.freelancehunt.ui.main.presentation.FeedViewModel
import tech.hombre.freelancehunt.ui.main.presentation.MainPublicViewModel
import tech.hombre.freelancehunt.ui.project.presentation.ProjectDetailViewModel

class FeedFragment : BaseFragment() {

    private val viewModel: FeedViewModel by viewModel()

    private val sharedViewModelMain: MainPublicViewModel by sharedViewModel()

    private val projectDetailsViewModel: ProjectDetailViewModel by sharedViewModel()

    override fun getLayout() = R.layout.fragment_feed

    override fun viewReady() {
        subscribeToData()
        viewModel.getFeedLists()
    }

    private fun subscribeToData() {
        viewModel.viewState.subscribe(this, ::handleViewState)
        projectDetailsViewModel.viewState.subscribe(this, {
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

    private fun handleViewState(viewState: ViewState<FeedList>) {
        when (viewState) {
            is Loading -> showLoading()
            is Success -> showFeedList(viewState.data)
            is Error -> handleError(viewState.error.localizedMessage)
            is NoInternetState -> showNoInternetError()
        }
    }

    private fun handleError(error: String) {
        hideLoading()
        showError(error, feedFragmentContainer)
    }

    private fun showNoInternetError() {
        hideLoading()
        snackbar(getString(R.string.no_internet_error_message), feedFragmentContainer)
    }


    private fun showFeedList(feedList: FeedList) {
        hideLoading()
        refresh.isRefreshing = false
        val adapter: RendererRecyclerViewAdapter?
        adapter = RendererRecyclerViewAdapter()
        adapter.registerRenderer(
            ViewRenderer<FeedList.Data, ViewFinder>(
                R.layout.item_feed_list,
                FeedList.Data::class.java,
                BaseViewRenderer.Binder<FeedList.Data, ViewFinder> { model: FeedList.Data, finder: ViewFinder, payloads: List<Any?>? ->
                    val type = feedTypeByMessage(context!!, model.attributes.message)
                    val typeIcon = ContextCompat.getDrawable(context!!, getTypeIcon(type))
                    val typeColor = ContextCompat.getColor(context!!, getTypeColor(type))
                    val typeText = getString(getTypeLabel(type))
                    val notForMe = model.attributes.from == null
                    finder
                        .find(
                            R.id.avatar,
                            ViewProvider<CustomImageView> { avatar ->
                                if (!notForMe) {
                                    finder.setGone(R.id.isplus, true)
                                    avatar.visible()
                                    finder.setGone(R.id.login, false)
                                    avatar.setUrl(
                                        model.attributes.from!!.avatar.small.url,
                                        isCircle = true
                                    )
                                } else {
                                    avatar.gone()
                                    finder.setGone(R.id.login, true)
                                    finder.setGone(R.id.isplus, false)
                                }
                            })
                        .setGone(R.id.isNew, !model.attributes.is_new)
                        .find(R.id.type, ViewProvider<TextView> { type ->
                            type.text = typeText
                            type.compoundDrawablePadding = 8
                            type.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                typeIcon,
                                null,
                                null,
                                null
                            )
                            type.background.setColorFilter(typeColor, PorterDuff.Mode.SRC_OVER)
                        })
                        .setText(R.id.login, model.attributes.from?.login ?: "")
                        .setText(
                            R.id.message,
                            model.attributes.message.prepareFeedMessage(context!!)
                        )
                        .setText(
                            R.id.createdAt,
                            model.attributes.created_at.parseFullDate(true).getTimeAgo()
                        )
                        .setOnClickListener(
                            R.id.clickableView
                        ) {
                            if (!notForMe) projectDetailsViewModel.getProjectDetails(model.links.project) else handleError(
                                getString(R.string.only_for_plus)
                            )
                        }
                }
            )
        )
        list.layoutManager = LinearLayoutManager(context)
        adapter.setItems(feedList.data)
        list.adapter = adapter

        sharedViewModelMain.setFeedBadgeCounter(feedList.data.filter { it.attributes.is_new }.size)

        refresh.setOnRefreshListener {
            adapter.setItems(arrayListOf())
            viewModel.getFeedLists()
        }
    }


    companion object {
        val TAG = FeedFragment::class.java.simpleName

        fun newInstance() = FeedFragment()
    }
}
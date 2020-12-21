package tech.hombre.freelancehunt.ui.main.view.fragments

import android.graphics.PorterDuff
import android.widget.TextView
import androidx.annotation.Keep
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.vivchar.rendererrecyclerviewadapter.*
import kotlinx.android.synthetic.main.fragment_feed.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.domain.model.FeedList
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.FeedType
import tech.hombre.freelancehunt.common.extensions.*
import tech.hombre.freelancehunt.common.provider.SchemeParser
import tech.hombre.freelancehunt.common.widgets.CustomImageView
import tech.hombre.freelancehunt.ui.base.*
import tech.hombre.freelancehunt.ui.base.ViewState
import tech.hombre.freelancehunt.ui.main.presentation.FeedViewModel
import tech.hombre.freelancehunt.ui.main.presentation.MainPublicViewModel

class FeedFragment : BaseFragment() {

    private val viewModel: FeedViewModel by viewModel()

    private val sharedViewModelMain: MainPublicViewModel by sharedViewModel()

    var items = FeedList()

    val adapter = RendererRecyclerViewAdapter()

    override fun getLayout() = R.layout.fragment_feed

    override fun viewReady() {
        initList()
        subscribeToData()
        viewModel.getFeedLists()
    }

    private fun initList() {
        adapter.registerRenderer(
            ViewRenderer<FeedList.Data, ViewFinder>(
                R.layout.item_feed_list,
                FeedList.Data::class.java,
                BaseViewRenderer.Binder<FeedList.Data, ViewFinder> { model: FeedList.Data, finder: ViewFinder, payloads: List<Any?>? ->
                    val type = feedTypeByMessage(requireContext(), model.attributes.message)
                    val typeIcon = ContextCompat.getDrawable(requireContext(), getTypeIcon(type))
                    val typeColor = ContextCompat.getColor(requireContext(), getTypeColor(type))
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
                                    finder
                                        .setGone(R.id.login, true)
                                        .setGone(R.id.isplus, false)
                                }
                            })
                        .setGone(R.id.isNew, !model.attributes.is_new)
                        .find(R.id.type, ViewProvider<TextView> { typeView ->
                            typeView.text = typeText
                            typeView.compoundDrawablePadding = 8
                            typeView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                typeIcon,
                                null,
                                null,
                                null
                            )
                            typeView.background.setColorFilter(typeColor, PorterDuff.Mode.SRC_OVER)
                        })
                        .setText(R.id.login, model.attributes.from?.login ?: "")
                        .find(R.id.message, ViewProvider<TextView> { messageView ->
                            if (type == FeedType.LIKE) {
                                messageView.compoundDrawablePadding = 8
                                messageView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                    ContextCompat.getDrawable(
                                        requireContext(),
                                        R.drawable.type_like
                                    ),
                                    null,
                                    null,
                                    null
                                )
                            } else {
                                messageView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                    null,
                                    null,
                                    null,
                                    null
                                )
                            }
                            messageView.text =
                                model.attributes.message.prepareFeedMessage(requireContext())
                        })
                        .setText(
                            R.id.createdAt,
                            model.attributes.created_at.parseFullDate(true).getTimeAgo()
                        )
                        .setOnClickListener(
                            R.id.clickableView
                        ) {
                            if (model.attributes.is_new) {
                                model.attributes.is_new = false
                                adapter.notifyItemChanged(items.data.indexOf(model))
                            }

                            if (type == FeedType.UNKNOWN) {
                                "<a href=\"(.*?)\".*".toRegex()
                                    .find(model.attributes.message)?.groupValues?.let {
                                        SchemeParser.launchUri(requireContext(), it[1])
                                    }
                            } else {
                                if (type == FeedType.PERSONAL_PROJECT) {
                                    "<a href=\"(.*?)\".*".toRegex()
                                        .find(model.attributes.message)?.groupValues?.let {
                                            SchemeParser.launchUri(requireContext(), it[1])
                                        }
                                } else if (!notForMe) {
                                    if (type != FeedType.UNKNOWN) viewModel.getProjectDetails(model.links.project)
                                } else handleError(
                                    getString(R.string.only_for_plus)
                                )
                            }
                        }
                }
            )
        )
    }

    private fun subscribeToData() {
        viewModel.viewState.subscribe(this, ::handleViewState)
        viewModel.feedMarked.subscribe(this, ::handleFeedViewState)
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

    private fun handleViewState(viewState: ViewState<FeedList>) {
        when (viewState) {
            is Loading -> showLoading()
            is Success -> showFeedList(viewState.data)
            is Error -> handleError(viewState.error.localizedMessage)
            is NoInternetState -> showNoInternetError()
        }
    }

    private fun handleFeedViewState(viewState: ViewState<Boolean>) {
        when (viewState) {
            is Success -> {
                hideLoading()
                sharedViewModelMain.setFeedBadgeCounter(0)
            }
            is Error -> handleError(viewState.error.localizedMessage)
            is NoInternetState -> showNoInternetError()
        }
    }

    private fun handleError(error: String) {
        refresh.isRefreshing = false
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

        list.layoutManager = LinearLayoutManager(context)
        list.adapter = adapter

        if (feedList.data.isNotEmpty()) {
            adapter.setItems(feedList.data)

            sharedViewModelMain.setFeedBadgeCounter(feedList.data.filter { it.attributes.is_new }.size)

            if (feedList.data.any { it.attributes.is_new }) viewModel.markFeedAsReaded()

            appPreferences.setLastFeedId(feedList.data.first().id)

            items = feedList
        }

        refresh.setOnRefreshListener {
            adapter.setItems(arrayListOf())
            viewModel.getFeedLists()
        }
    }


    companion object {
        @Keep
        val TAG = FeedFragment::class.java.simpleName

        fun newInstance() = FeedFragment()
    }
}
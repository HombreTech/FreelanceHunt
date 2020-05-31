package tech.hombre.freelancehunt.ui.contest.view.pager

import android.os.Bundle
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.vivchar.rendererrecyclerviewadapter.*
import kotlinx.android.synthetic.main.fragment_pager_contest_comments.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.domain.model.ContestComment
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.common.extensions.getTimeAgo
import tech.hombre.freelancehunt.common.extensions.parseFullDate
import tech.hombre.freelancehunt.common.extensions.snackbar
import tech.hombre.freelancehunt.common.extensions.subscribe
import tech.hombre.freelancehunt.common.widgets.CustomImageView
import tech.hombre.freelancehunt.ui.base.*
import tech.hombre.freelancehunt.ui.base.ViewState
import tech.hombre.freelancehunt.ui.contest.presentation.ContestCommentsViewModel
import tech.hombre.freelancehunt.ui.contest.presentation.ContestPublicViewModel

class PagerContestComments : BaseFragment() {
    override fun getLayout() = R.layout.fragment_pager_contest_comments

    private val viewModel: ContestCommentsViewModel by viewModel()

    private val contestPublicViewModel: ContestPublicViewModel by sharedViewModel()

    lateinit var adapter: RendererRecyclerViewAdapter

    private var projectId = 0

    override fun viewReady() {
        arguments?.let {
            projectId = it.getInt(EXTRA_1)
            if (projectId != 0) {
                initList()
                subscribeToData()
                viewModel.getContestComment(projectId)
            }
        }
    }

    private fun initList() {
        adapter = RendererRecyclerViewAdapter()
        adapter.enableDiffUtil(true)
        adapter.registerRenderer(
            ViewRenderer(
                R.layout.item_contest_comments_list,
                ContestComment.Data::class.java,
                BaseViewRenderer.Binder { model: ContestComment.Data, finder: ViewFinder, payloads: List<Any?>? ->
                    finder
                        .find<CardView>(R.id.mainView) {
                            it.updateLayoutParams<ConstraintLayout.LayoutParams> {
                                marginStart = (model.attributes.level - 1) * 50
                            }
                        }
                        .find(
                            R.id.avatar,
                            ViewProvider<CustomImageView> { avatar ->
                                avatar.setUrl(
                                    model.attributes.author.avatar.small.url,
                                    isCircle = true
                                )
                            })
                        .setText(
                            R.id.login,
                            model.attributes.author.let { if (it.first_name.isBlank()) it.login else it.first_name + " " + it.last_name })
                        .setText(
                            R.id.postedAt,
                            model.attributes.posted_at.parseFullDate(true).getTimeAgo()
                        )
                        .setText(R.id.comment, model.attributes.message)
                        .setOnClickListener(R.id.clickableView) {
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
    }

    private fun subscribeToData() {
        viewModel.viewState.subscribe(this, ::handleViewState)
    }

    private fun handleViewState(viewState: ViewState<ContestComment>) {
        when (viewState) {
            is Loading -> showLoading()
            is Success -> initBids(viewState.data.data)
            is Error -> handleError(viewState.error.localizedMessage)
            is NoInternetState -> showNoInternetError()
        }
    }

    private fun handleError(error: String) {
        hideLoading()
        showError(error, commentsContainer)
    }

    private fun showNoInternetError() {
        hideLoading()
        snackbar(getString(R.string.no_internet_error_message), commentsContainer)
    }

    private fun initBids(comments: List<ContestComment.Data>) {
        hideLoading()
        adapter.setItems(comments)
        contestPublicViewModel.updateBadge(1, comments.size)
    }

    companion object {
        val TAG = PagerContestComments::class.java.simpleName

        fun newInstance(profileId: Int): PagerContestComments {
            val fragment = PagerContestComments()
            val extra = Bundle()
            extra.putInt(EXTRA_1, profileId)
            fragment.arguments = extra
            return fragment
        }

    }
}
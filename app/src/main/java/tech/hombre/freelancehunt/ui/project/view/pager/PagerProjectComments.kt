package tech.hombre.freelancehunt.ui.project.view.pager

import android.os.Bundle
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.vivchar.rendererrecyclerviewadapter.*
import kotlinx.android.synthetic.main.fragment_pager_project_comments.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.domain.model.ProjectComment
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.common.UserType
import tech.hombre.freelancehunt.common.extensions.getTimeAgo
import tech.hombre.freelancehunt.common.extensions.parseFullDate
import tech.hombre.freelancehunt.common.extensions.snackbar
import tech.hombre.freelancehunt.common.extensions.subscribe
import tech.hombre.freelancehunt.common.widgets.CustomImageView
import tech.hombre.freelancehunt.ui.base.*
import tech.hombre.freelancehunt.ui.base.ViewState
import tech.hombre.freelancehunt.ui.project.presentation.ProjectCommentsViewModel
import tech.hombre.freelancehunt.ui.project.presentation.ProjectPublicViewModel

class PagerProjectComments : BaseFragment() {
    override fun getLayout() = R.layout.fragment_pager_project_comments

    private val viewModel: ProjectCommentsViewModel by viewModel()

    private val projectPublicViewModel: ProjectPublicViewModel by sharedViewModel()

    lateinit var adapter: RendererRecyclerViewAdapter

    private var projectId = 0

    override fun viewReady() {
        arguments?.let {
            projectId = it.getInt(EXTRA_1)
            if (projectId != 0) {
                initList()
                subscribeToData()
                viewModel.getProjectComment(projectId)
            }
        }
    }

    private fun initList() {
        adapter = RendererRecyclerViewAdapter()
        adapter.enableDiffUtil(true)
        adapter.registerRenderer(
            ViewRenderer(
                R.layout.item_project_comments_list,
                ProjectComment.Data::class.java,
                BaseViewRenderer.Binder { model: ProjectComment.Data, finder: ViewFinder, payloads: List<Any?>? ->
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
                            if (model.attributes.author.type == UserType.EMPLOYER.type) viewModel.getEmployerDetails(
                                model.attributes.author.id
                            ) else viewModel.getFreelancerDetails(model.attributes.author.id)
                        }
                }
            )
        )
        list.layoutManager = LinearLayoutManager(activity)
        list.adapter = adapter
    }

    private fun subscribeToData() {
        viewModel.viewState.subscribe(this, ::handleViewState)
        viewModel.freelancerDetails.subscribe(this, {
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
    }

    private fun handleViewState(viewState: ViewState<ProjectComment>) {
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

    private fun initBids(comments: List<ProjectComment.Data>) {
        hideLoading()
        adapter.setItems(comments)
        projectPublicViewModel.updateBadge(2, comments.size)
    }

    companion object {
        val TAG = PagerProjectComments::class.java.simpleName

        fun newInstance(profileId: Int): PagerProjectComments {
            val fragment = PagerProjectComments()
            val extra = Bundle()
            extra.putInt(EXTRA_1, profileId)
            fragment.arguments = extra
            return fragment
        }

    }
}
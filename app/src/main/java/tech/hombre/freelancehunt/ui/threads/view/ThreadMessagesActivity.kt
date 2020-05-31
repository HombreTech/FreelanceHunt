package tech.hombre.freelancehunt.ui.threads.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.vivchar.rendererrecyclerviewadapter.*
import kotlinx.android.synthetic.main.activity_thread_messages.*
import kotlinx.android.synthetic.main.appbar_fill.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.domain.model.ThreadMessageList
import tech.hombre.domain.model.ThreadMessageMy
import tech.hombre.domain.model.ThreadMessageOther
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.common.extensions.getTimeAgo
import tech.hombre.freelancehunt.common.extensions.parseFullDate
import tech.hombre.freelancehunt.common.extensions.snackbar
import tech.hombre.freelancehunt.common.extensions.subscribe
import tech.hombre.freelancehunt.common.widgets.CustomImageView
import tech.hombre.freelancehunt.ui.base.*
import tech.hombre.freelancehunt.ui.base.ViewState
import tech.hombre.freelancehunt.ui.threads.presentation.ThreadMessagesViewModel

class ThreadMessagesActivity : BaseActivity() {

    override fun isPrivate() = false

    private val viewModel: ThreadMessagesViewModel by viewModel()

    private var threadId = 0

    lateinit var adapter: RendererRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thread_messages)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (savedInstanceState == null) {
            threadId = intent?.extras?.getInt(EXTRA_1, -1) ?: -1
        }

        subscribeToData()
        viewModel.getMessages(threadId)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_thread, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun subscribeToData() {
        viewModel.viewState.subscribe(this, ::handleViewState)
    }

    private fun handleViewState(viewState: ViewState<ThreadMessageList>) {
        when (viewState) {
            is Loading -> showLoading(threadLoadingProgress)
            is Success -> initMessagesList(viewState.data.data)
            is Error -> handleError(viewState.error.localizedMessage)
            is NoInternetState -> showNoInternetError()
        }
    }

    private fun initMessagesList(messages: List<ThreadMessageList.Data>) {
        val messages_new = messages.map {
            if (it.attributes.participants.from.login == getCurrentUser()) ThreadMessageMy(it) else ThreadMessageOther(
                it
            )
        }

        hideLoading(threadLoadingProgress)
        refresh.isRefreshing = false
        adapter = RendererRecyclerViewAdapter()
        adapter.enableDiffUtil(true)
        adapter.registerRenderer(
            ViewRenderer(
                R.layout.item_thread_message_my,
                ThreadMessageMy::class.java,
                BaseViewRenderer.Binder { model: ThreadMessageMy, finder: ViewFinder, payloads: List<Any?>? ->
                    finder
                        .find(
                            R.id.avatar,
                            ViewProvider<CustomImageView> { avatar ->
                                avatar.setUrl(
                                    model.data.attributes.participants.from.avatar.small.url,
                                    isCircle = true
                                )
                            })
                        .setText(R.id.text, model.data.attributes.message_html)
                        .setText(
                            R.id.postedAt,
                            model.data.attributes.posted_at.parseFullDate(true).getTimeAgo()
                        )
                }
            )
        )
        adapter.registerRenderer(
            ViewRenderer(
                R.layout.item_thread_message,
                ThreadMessageOther::class.java,
                BaseViewRenderer.Binder { model: ThreadMessageOther, finder: ViewFinder, payloads: List<Any?>? ->
                    finder
                        .find(
                            R.id.avatar,
                            ViewProvider<CustomImageView> { avatar ->
                                avatar.setUrl(
                                    model.data.attributes.participants.from.avatar.small.url,
                                    isCircle = true
                                )
                            })
                        .setText(R.id.text, model.data.attributes.message_html)
                        .setText(
                            R.id.postedAt,
                            model.data.attributes.posted_at.parseFullDate(true).getTimeAgo()
                        )
                }
            )
        )
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter

        adapter.setItems(messages_new)

        refresh.setOnRefreshListener {
            viewModel.getMessages(threadId)
        }
    }

    private fun handleError(error: String) {
        hideLoading(threadLoadingProgress)
        showError(error)
    }

    private fun showNoInternetError() {
        hideLoading(threadLoadingProgress)
        snackbar(getString(R.string.no_internet_error_message), threadActivityContainer)
    }

    companion object {

        fun startActivity(activity: Activity, threadId: Int) {
            val intent = Intent(activity, ThreadMessagesActivity::class.java)
            intent.putExtra(EXTRA_1, threadId)
            activity.startActivity(intent)
        }
    }

}
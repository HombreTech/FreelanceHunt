package tech.hombre.freelancehunt.ui.threads.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.vivchar.rendererrecyclerviewadapter.*
import kotlinx.android.synthetic.main.activity_thread_messages.*
import kotlinx.android.synthetic.main.appbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter
import org.sufficientlysecure.htmltextview.HtmlTextView
import tech.hombre.domain.model.ThreadMessageList
import tech.hombre.domain.model.ThreadMessageMy
import tech.hombre.domain.model.ThreadMessageOther
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.common.EXTRA_2
import tech.hombre.freelancehunt.common.extensions.*
import tech.hombre.freelancehunt.common.widgets.CustomImageView
import tech.hombre.freelancehunt.ui.base.*
import tech.hombre.freelancehunt.ui.base.ViewState
import tech.hombre.freelancehunt.ui.threads.presentation.ThreadMessagesViewModel
import java.util.*
import kotlin.collections.ArrayList

class ThreadMessagesActivity : BaseActivity() {

    override fun isPrivate() = false

    private val viewModel: ThreadMessagesViewModel by viewModel()

    private var threadId = 0

    private var threadUrl = ""

    lateinit var adapter: RendererRecyclerViewAdapter

    private var messagesGroup = arrayListOf<ViewModel>()

    private var messages = arrayListOf<ThreadMessageList.Data>()

    private var timer = Timer()

    // TODO to preferences
    private val delay = 15000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thread_messages)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (savedInstanceState == null) {
            threadId = intent?.extras?.getInt(EXTRA_1, -1) ?: -1
            threadUrl = intent?.extras?.getString(EXTRA_2, "") ?: ""
        }

        subscribeToData()
        initMessagesList()
        viewModel.getMessages(threadId)
        initViews()
    }

    private fun initViews() {
        attach.setOnClickListener {
            handleError("Not implemented yet :(")
        }
        send.setOnClickListener {
            list.hideKeyboard()
            if (correctInputs()) {
                viewModel.sendMessage(threadId, editText.savedText.toString())
            }
        }
    }

    private fun correctInputs(): Boolean {
        return editText.savedText.isNotEmpty()
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
            R.id.action_share -> shareUrl(this, threadUrl)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun subscribeToData() {
        viewModel.viewState.subscribe(this, ::handleViewState)
        viewModel.message.subscribe(this, ::handleMessageViewState)
    }

    private fun handleViewState(viewState: ViewState<ThreadMessageList>) {
        when (viewState) {
            is Loading -> showLoading(progressBar)
            is Success -> initMessages(viewState.data.data)
            is Error -> handleError(viewState.error.localizedMessage)
            is NoInternetState -> showNoInternetError()
        }
    }

    private fun handleMessageViewState(viewState: ViewState<ThreadMessageList.Data>) {
        when (viewState) {
            is Success -> addMessage(viewState.data)
        }
    }

    private fun addMessage(message: ThreadMessageList.Data) {
        messagesGroup.add(ThreadMessageMy(message))
        adapter.setItems(messagesGroup)
        list.postDelayed(
            { list.scrollToPosition(adapter.itemCount - 1) },
            100
        )

    }

    private fun initMessagesList() {
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
                        .find<HtmlTextView>(R.id.text) {
                            val getter = HtmlHttpImageGetter(it, null, true).apply {
                                enableCompressImage(true, 70)
                            }
                            it.setHtml(model.data.attributes.message_html, getter)
                        }
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
                        .find<HtmlTextView>(R.id.text) {
                            val getter = HtmlHttpImageGetter(it, null, false).apply {
                                enableCompressImage(true, 70)
                            }
                            it.setHtml(model.data.attributes.message_html, getter)
                        }
                        .setText(
                            R.id.postedAt,
                            model.data.attributes.posted_at.parseFullDate(true).getTimeAgo()
                        )
                }
            )
        )
        list.layoutManager = LinearLayoutManager(this).apply { stackFromEnd = true }
        list.adapter = adapter

        refresh.setOnRefreshListener {
            viewModel.getMessages(threadId)
        }

        timer.schedule(timerTask, delay, delay)
    }

    private fun initMessages(messages: List<ThreadMessageList.Data>) {
        hideLoading(progressBar)
        refresh.isRefreshing = false
        this.messages = messages as ArrayList<ThreadMessageList.Data>
        messagesGroup = messages.map {
            if (it.attributes.participants.from.login == getCurrentUser()) ThreadMessageMy(it) else ThreadMessageOther(
                it
            )
        } as ArrayList<ViewModel>
        adapter.setItems(messagesGroup)
    }

    private fun handleError(error: String) {
        hideLoading(progressBar)
        showError(error)
    }

    private fun showNoInternetError() {
        hideLoading(progressBar)
        snackbar(getString(R.string.no_internet_error_message), threadActivityContainer)
    }

    private val timerTask = object : TimerTask() {
        override fun run() {
            runOnUiThread {
                viewModel.getMessages(threadId)
            }
        }
    }

    override fun finish() {
        timer.cancel()
        timer.purge()
        super.finish()
    }

    companion object {

        fun startActivity(activity: Activity, threadId: Int, threadUrl: String) {
            val intent = Intent(activity, ThreadMessagesActivity::class.java)
            intent.putExtra(EXTRA_1, threadId)
            intent.putExtra(EXTRA_2, threadUrl)
            activity.startActivity(intent)
        }
    }

}
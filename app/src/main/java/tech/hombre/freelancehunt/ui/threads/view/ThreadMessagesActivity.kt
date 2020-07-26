package tech.hombre.freelancehunt.ui.threads.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.vivchar.rendererrecyclerviewadapter.*
import kotlinx.android.synthetic.main.activity_thread_messages.*
import kotlinx.android.synthetic.main.appbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.domain.model.ThreadMessageList
import tech.hombre.domain.model.ThreadMessageMy
import tech.hombre.domain.model.ThreadMessageOther
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.common.EXTRA_2
import tech.hombre.freelancehunt.common.UserType
import tech.hombre.freelancehunt.common.extensions.*
import tech.hombre.freelancehunt.common.widgets.CustomHtmlTextView
import tech.hombre.freelancehunt.common.widgets.CustomImageView
import tech.hombre.freelancehunt.ui.base.*
import tech.hombre.freelancehunt.ui.base.ViewState
import tech.hombre.freelancehunt.ui.threads.presentation.ThreadMessagesViewModel
import java.util.*


class ThreadMessagesActivity : BaseActivity() {

    private val viewModel: ThreadMessagesViewModel by viewModel()

    private var threadId = 0

    private var threadUrl = ""

    lateinit var adapter: RendererRecyclerViewAdapter

    private var messagesGroup = arrayListOf<ViewModel>()

    private var messages = arrayListOf<ThreadMessageList.Data>()

    private var timer = Timer()

    // TODO to preferences
    private val delay = 15000L

    override fun viewReady() {
        setContentView(R.layout.activity_thread_messages)
        setSupportActionBar(toolbar)
        setTitle(R.string.thread_view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        intent?.extras?.let {
            threadId = it.getInt(EXTRA_1, -1)
            threadUrl = it.getString(EXTRA_2, "") ?: ""
            subscribeToData()
            initMessagesList()
            viewModel.getMessages(threadId)
            initViews()
        }
    }

    private fun initViews() {
        attach.setOnClickListener {
            handleError(getString(R.string.not_implemented_error))
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
            R.id.action_open -> openUrl(this, threadUrl)
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
        editText.setText("")
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
                                avatar.setOnClickListener {
                                    if (model.data.attributes.participants.from.type == UserType.EMPLOYER.type)  {
                                        appNavigator.showEmployerDetails(model.data.attributes.participants.from.id)
                                    } else appNavigator.showFreelancerDetails(model.data.attributes.participants.from.id)
                                }
                            })
                        .find<CustomHtmlTextView>(R.id.text) {
                            it.setHtmlText(model.data.attributes.message_html, false)
                        }
                        .setText(
                            R.id.postedAt,
                            model.data.attributes.posted_at.parseFullDate(true).getTimeAgo()
                        )


                    if (model.data.attributes.attachments.isNotEmpty()) {
                        val attachmentsAdapter = RendererRecyclerViewAdapter()
                        attachmentsAdapter.registerRenderer(
                            ViewRenderer(
                                R.layout.item_threads_attachment_my,
                                ThreadMessageList.Data.Attributes.Attachment::class.java,
                                BaseViewRenderer.Binder { model: ThreadMessageList.Data.Attributes.Attachment, finder: ViewFinder, payloads: List<Any?>? ->
                                    finder
                                        .find(
                                            R.id.thumbnail,
                                            ViewProvider<CustomImageView> { thumbnail ->
                                                if (model.thumbnail_url.isNullOrEmpty()) {
                                                    thumbnail.setUrlSVG(
                                                        "https://freelancehunt.com/static/images/file-types/${getFileTypeByExtension(
                                                            model.url.extension()
                                                        )}.svg"
                                                    )
                                                } else thumbnail.setUrl(
                                                    model.thumbnail_url ?: ""
                                                )
                                            })
                                        .setText(
                                            R.id.title,
                                            model.name
                                        )
                                        .setText(
                                            R.id.size,
                                            model.size.toLong().humanReadableBytes()
                                        )
                                        .setOnClickListener {
                                            val browserIntent = Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse(model.url)
                                            )
                                            startActivity(browserIntent)
                                        }
                                }
                            )
                        )
                        finder.find<RecyclerView>(R.id.attachments).adapter = attachmentsAdapter
                        attachmentsAdapter.setItems(model.data.attributes.attachments)
                    }

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
                                avatar.setOnClickListener {
                                    if (model.data.attributes.participants.from.type == UserType.EMPLOYER.type)  {
                                        appNavigator.showEmployerDetails(model.data.attributes.participants.from.id)
                                    } else appNavigator.showFreelancerDetails(model.data.attributes.participants.from.id)
                                }
                            })
                        .find<CustomHtmlTextView>(R.id.text) {
                            it.setHtmlText(model.data.attributes.message_html, false)
                        }
                        .setText(
                            R.id.postedAt,
                            model.data.attributes.posted_at.parseFullDate(true).getTimeAgo()
                        )
                    if (model.data.attributes.attachments.isNotEmpty()) {
                        val attachmentsAdapter = RendererRecyclerViewAdapter()
                        attachmentsAdapter.registerRenderer(
                            ViewRenderer(
                                R.layout.item_threads_attachment,
                                ThreadMessageList.Data.Attributes.Attachment::class.java,
                                BaseViewRenderer.Binder { model: ThreadMessageList.Data.Attributes.Attachment, finder: ViewFinder, payloads: List<Any?>? ->
                                    finder
                                        .find(
                                            R.id.thumbnail,
                                            ViewProvider<CustomImageView> { thumbnail ->
                                                if (model.thumbnail_url.isNullOrEmpty()) {
                                                    thumbnail.setUrlSVG(
                                                        "https://freelancehunt.com/static/images/file-types/${getFileTypeByExtension(
                                                            model.url.extension()
                                                        )}.svg"
                                                    )
                                                } else thumbnail.setUrl(
                                                    model.thumbnail_url ?: ""
                                                )
                                            })
                                        .setText(
                                            R.id.title,
                                            model.name
                                        )
                                        .setText(
                                            R.id.size,
                                            model.size.toLong().humanReadableBytes()
                                        )
                                        .setOnClickListener {
                                            val browserIntent = Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse(model.url)
                                            )
                                            startActivity(browserIntent)
                                        }
                                }
                            )
                        )
                        finder.find<RecyclerView>(R.id.attachments).adapter = attachmentsAdapter
                        attachmentsAdapter.setItems(model.data.attributes.attachments)
                    }
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

        fun startActivity(context: Context, threadId: Int, threadUrl: String) {
            val intent = Intent(context, ThreadMessagesActivity::class.java)
            intent.putExtra(EXTRA_1, threadId)
            intent.putExtra(EXTRA_2, threadUrl)
            context.startActivity(intent)
        }
    }

}
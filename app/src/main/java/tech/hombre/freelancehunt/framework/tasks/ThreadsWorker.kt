package tech.hombre.freelancehunt.framework.tasks

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tech.hombre.data.local.LocalProperties
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.domain.repository.ThreadsListRepository
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.MAX_LINES
import tech.hombre.freelancehunt.common.NOTIFY_MESSAGE_ID
import tech.hombre.freelancehunt.common.extensions.getEnding
import tech.hombre.freelancehunt.common.extensions.parseFullDate
import tech.hombre.freelancehunt.framework.notifications.AndroidNotificationService
import tech.hombre.freelancehunt.framework.notifications.SimpleNotification
import tech.hombre.freelancehunt.routing.ScreenType


class ThreadsWorker(
    val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    private val threadsRepository: ThreadsListRepository by inject()

    private val appPreferences: LocalProperties by inject()

    private val notificationService: AndroidNotificationService by inject()

    private val tasksManger: TasksManger by inject()

    override suspend fun doWork(): Result {
        try {
            Log.d("ThreadsWorker", "doWork")
            if (appPreferences.getCurrentUserId() != -1) {
                threadsRepository.getThreadsList("threads")
                    .onSuccess {
                        val lastChecked = appPreferences.getLastMessageId()
                        val new =
                            it.data.filter { thread ->
                                val time = thread.attributes.last_post_at.parseFullDate(true)?.time ?: 0
                                thread.attributes.is_unread && time > lastChecked
                            }
                        if (new.isNotEmpty()) {
                            val lastMessageId = new.first().attributes.last_post_at.parseFullDate(true)?.time ?: 0
                            appPreferences.setLastMessageId(lastMessageId)
                            Log.d("ThreadsWorker", "Notify")
                            notificationService.notify(
                                SimpleNotification(
                                    String.format(
                                        context.getString(R.string.notify_new_message),
                                        new.size.getEnding(context, R.array.ending_messages)
                                    ),
                                    new.map { "<b>${it.attributes.participants.from.login}:</b> <i>${it.attributes.subject}</i>" }
                                        .chunked(MAX_LINES)[0],
                                    ScreenType.THREADS,
                                    new.size - MAX_LINES,
                                    NOTIFY_MESSAGE_ID
                                )
                            )
                        }
                    }
                    .onFailure { Log.e("ThreadsWorker", it.throwable.message, it.throwable) }
            } else Log.d("FeedWorker", "Skip")
            tasksManger.recreateTasks(messages = true)
            return Result.success()
        } catch (e: Exception) {
            Log.e("ThreadsWorker", e.message, e)
            return Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "ThreadsWorker"
    }


}


